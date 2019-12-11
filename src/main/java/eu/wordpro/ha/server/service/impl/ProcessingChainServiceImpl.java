package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.InvalidSignalException;
import eu.wordpro.ha.api.SignalProcessingException;
import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.repository.ProcessingChainRepository;
import eu.wordpro.ha.server.service.DeviceService;
import eu.wordpro.ha.server.service.ProcessingChainService;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;
import eu.wordpro.ha.server.service.mapper.ProcessingChainMapper;
import eu.wordpro.ha.server.service.mqtt.MqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProcessingChainServiceImpl implements ProcessingChainService {

    @Autowired
    ProcessingChainRepository processingChainRepository;

    @Autowired
    ProcessingChainMapper processingChainMapper;

    @Autowired
    SignalProcessorInstancesManager signalProcessorInstancesManager;

    @Autowired
    MqttClient mqttClient;

    @Autowired
    @Lazy
    DeviceService deviceService;

    Logger logger = LoggerFactory.getLogger(ProcessingChainServiceImpl.class);

    @Override
    public ProcessingChainDTO save(ProcessingChainDTO spaceDTO) {
        ProcessingChain processingChain = processingChainMapper.toEntity(spaceDTO);
        processingChain = processingChainRepository.save(processingChain);
        return processingChainMapper.toDto(processingChain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProcessingChainDTO> findOne(Long spaceId) {
        return processingChainRepository.findById(spaceId).map(processingChainMapper::toDto);
    }

    @Override
    public List<ProcessingChainDTO> findAll() {
        return processingChainRepository.findAll()
                .stream()
                .map(processingChainMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        processingChainRepository.deleteById(id);
    }

    @Override
    public void executeProcessingChain(ProcessingChain processingChain, LinkedList<SignalProcessorData> inputs) {
        if (processingChain == null) {
            return;
        }
        SignalProcessor signalProcessorDesc = processingChain.getSignalProcessor();
        if (signalProcessorDesc == null) {
            return;
        }
        eu.wordpro.ha.api.SignalProcessor instance = signalProcessorInstancesManager.getInstance(signalProcessorDesc);
        if (instance == null) {
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
        if (result != null) {
            result.setName(signalProcessorDesc.getName());
            inputs.add(result);
        }
        sendDataToOuputDevice(processingChain, result);
        executeProcessingChain(processingChain.getNext(), inputs);
    }

    @Override
    public void sendDataToOuputDevice(ProcessingChain processingChain, SignalProcessorData result) {
        if (processingChain.getOutputDevice() != null) {
            String topic = deviceService.getMqttTopic(processingChain.getOutputDevice().getId()) + "/down";
            logger.debug("Sending message to topic {}", topic);
            mqttClient.sendData(result.toBytes(), topic);
        }
    }
}
