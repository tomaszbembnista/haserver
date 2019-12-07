package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.InvalidSignalException;
import eu.wordpro.ha.api.SignalProcessingException;
import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.StringSignalProcessorData;
import eu.wordpro.ha.server.domain.Device;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.domain.Space;
import eu.wordpro.ha.server.repository.DeviceRepository;
import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.dto.DeviceDTO;
import eu.wordpro.ha.server.service.mapper.DeviceMapper;
import eu.wordpro.ha.server.service.mqtt.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    SignalProcessorInstancesManager signalProcessorInstancesManager;

    @Lazy
    @Autowired
    private MqttClient mqttClient;

    Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDTO> findOne(Long id) {
        return deviceRepository.findById(id).map(deviceMapper::toDto);
    }

    @Override
    public List<DeviceDTO> findAll() {
        return deviceRepository.findAll()
                .stream()
                .map(deviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public String getMqttTopic(Long deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null){
            logger.warn("Device with id {} not found", deviceId);
            return null;
        }
        String slug = device.getSlug();
        Space space = device.getSpace();
        while (space != null){
            slug = space.getSlug() + "/" + slug;
            space = space.getParent();
        }
        return slug;
    }

    @Override
    public void executeProcessingChains(Long deviceId, LinkedList<SignalProcessorData> inputs) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        if (device == null){
            logger.warn("Device with id {} not found", deviceId);
            return;
        }
        Set<ProcessingChain> processingChains = device.getProcessingChains();
        for (ProcessingChain processingChain : processingChains){
            LinkedList<SignalProcessorData> newInputs = new LinkedList<>(inputs);
            executeProcessingChain(processingChain, newInputs);
        }
    }

    private void executeProcessingChain(ProcessingChain processingChain, LinkedList<SignalProcessorData> inputs) {
        if (processingChain == null){
            return;
        }
        SignalProcessor signalProcessorDesc = processingChain.getSignalProcessor();
        if (signalProcessorDesc == null){
            return;
        }
        eu.wordpro.ha.api.SignalProcessor instance = signalProcessorInstancesManager.getInstance(signalProcessorDesc);
        if (instance == null){
            return;
        }
        logger.debug("Signal processor {}:{} instantiated", signalProcessorDesc.getName(), signalProcessorDesc.getClassName());
        SignalProcessorData result;
        try {
            result = instance.processInput(inputs);
        } catch (InvalidSignalException e) {
            logger.warn("Signal is not valid. Error: {}", e.getMessage());
            return;
        } catch (SignalProcessingException e) {
            logger.warn("Signal could not be processed. Error: {}", e.getMessage());
            return;
        }
        logger.debug("Signal processor {} done.", signalProcessorDesc.getName());
        if (result != null){
            result.setName(signalProcessorDesc.getName());
            inputs.add(result);
        }
        if (processingChain.getOutputDevice() != null){
            String topic = getMqttTopic(processingChain.getOutputDevice().getId()) + "/down";
            logger.debug("Sending message to topic {}", topic);
            mqttClient.sendData(result.toBytes(), topic);
        }
        executeProcessingChain(processingChain.getNext(), inputs);
        
    }
}
