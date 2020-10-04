package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.InvalidSignalException;
import eu.wordpro.ha.api.SignalProcessingException;
import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.server.domain.Device;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.repository.ProcessingChainRepository;
import eu.wordpro.ha.server.rest.error.NotFoundException;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
        ProcessingChain elemToDelete = processingChainRepository.findById(id).orElseThrow(() -> new RuntimeException());
        Device deviceFeedingTheElement = elemToDelete.getInputDevice();
        ProcessingChain nextInProcessingChain = elemToDelete.getNext();
        if (deviceFeedingTheElement != null && nextInProcessingChain != null) { //element is first one in processing chain, attaching next one to device
            nextInProcessingChain.setInputDevice(deviceFeedingTheElement);
            processingChainRepository.save(nextInProcessingChain);
        }
        List<ProcessingChain> allReferencingStep = processingChainRepository.findAllByNextId(id);
        for (ProcessingChain step: allReferencingStep) {
            step.setNext(nextInProcessingChain);
            processingChainRepository.save(step);
        }
        processingChainRepository.deleteById(id);
    }

    @Override
    public void executeProcessingChain(ProcessingChain processingChain, ProcessingData inputs) {
        logger.info("Executing processing chain element.");
        if (processingChain == null) {
            logger.info("Processing chain element is null.");
            return;
        }
        SignalProcessor signalProcessorDesc = processingChain.getSignalProcessor();
        if (signalProcessorDesc == null) {
            logger.warn("Description of signal processor in next chain element is null");
            return;
        }
        eu.wordpro.ha.api.SignalProcessor instance = signalProcessorInstancesManager.getInstance(signalProcessorDesc);
        logger.info("Dupa kupa {}", instance);
        if (instance == null) {
            logger.warn("Instance of signal processor in next chain element is null");
            return;
        }
        logger.info("Dupa kupa 1");
        logger.info("Signal processor {}:{} instantiated", signalProcessorDesc.getName(), signalProcessorDesc.getClassName());
        SignalProcessorData result;
        try {
            logger.info("Dupa kupa 11");
            result = instance.processInput(inputs);
            logger.info("Dupa kupa 111");
        } catch (InvalidSignalException e) {
            logger.info("Dupa kupa 2");
            logger.warn("Signal is not valid. Error: {}", e.getMessage());
            return;
        } catch (SignalProcessingException e) {
            logger.info("Dupa kupa 3");
            logger.warn("Signal could not be processed. Error: {}", e.getMessage());
            return;
        }
        catch (Exception e) {
            logger.info("Dupa kupa 33");
            e.printStackTrace();
            return;
        }
        logger.info("Signal processor {} done.", signalProcessorDesc.getName());
        if (result != null) {
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
            try {
                mqttClient.sendData(result.getValue().getBytes("UTF-8"), topic);
            } catch (UnsupportedEncodingException e) {
                logger.error("Exception during message sending.", e);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessingChainDTO> findProcessingChainsByInputDevice(Long deviceId) {
        return processingChainRepository.findAllByInputDeviceId(deviceId)
                .stream()
                .map(processingChainMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessingChainDTO> findAllStepsOfProcessingChain(Long rootId) {
        List<ProcessingChain> allSteps = new ArrayList<>();
        ProcessingChain currentItem = processingChainRepository.findById(rootId).orElseThrow(() -> new NotFoundException());
        allSteps.add(currentItem);
        currentItem = currentItem.getNext();
        while (currentItem != null) {
            allSteps.add(currentItem);
            currentItem = currentItem.getNext();
        }
        return allSteps.stream()
                .map(processingChainMapper::toDto)
                .collect(Collectors.toList());
    }
}
