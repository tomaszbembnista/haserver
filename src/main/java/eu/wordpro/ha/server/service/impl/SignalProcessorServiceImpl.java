package eu.wordpro.ha.server.service.impl;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.OperationExecutionResult;
import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.domain.SignalProcessor;
import eu.wordpro.ha.server.repository.SignalProcessorRepository;
import eu.wordpro.ha.server.rest.error.NotFoundException;
import eu.wordpro.ha.server.service.ProcessingChainService;
import eu.wordpro.ha.server.service.SignalProcessorService;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;
import eu.wordpro.ha.server.service.mapper.SignalProcessorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SignalProcessorServiceImpl implements SignalProcessorService {

    @Autowired
    SignalProcessorRepository signalProcessorRepository;

    @Autowired
    SignalProcessorMapper signalProcessorMapper;

    @Autowired
    SignalProcessorInstancesManager signalProcessorInstancesManager;

    @Autowired
    ProcessingChainService processingChainService;

    @Autowired
    ThreadPoolsProvider threadPoolsProvider;

    @Override
    public SignalProcessorDTO save(SignalProcessorDTO signalProcessorDTO) {
        SignalProcessor signalProcessor = signalProcessorMapper.toEntity(signalProcessorDTO);
        signalProcessor = signalProcessorRepository.save(signalProcessor);
        return signalProcessorMapper.toDto(signalProcessor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SignalProcessorDTO> findOne(Long signalProcessorId) {
        return signalProcessorRepository.findById(signalProcessorId).map(signalProcessorMapper::toDto);
    }

    @Override
    public List<SignalProcessorDTO> findAll() {
        return signalProcessorRepository.findAll()
                .stream()
                .map(signalProcessorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        signalProcessorRepository.deleteById(id);
    }

    @Override
    public List<ProcessorOperationDesc> getPossibleOperations(Long processorId) {
        SignalProcessor signalProcessor = signalProcessorRepository.findById(processorId).orElseThrow(() -> new NotFoundException());
        eu.wordpro.ha.api.SignalProcessor instance = signalProcessorInstancesManager.getInstance(signalProcessor);
        if (instance != null) {
            return instance.listPossibleOperations();
        }
        return new ArrayList<>();
    }

    @Override
    public String executeOperation(Long processorId, String operationName, List<ProcessorOperationArgument> arguments) {
        SignalProcessor signalProcessor = signalProcessorRepository.findById(processorId).orElseThrow(() -> new NotFoundException());
        eu.wordpro.ha.api.SignalProcessor instance = signalProcessorInstancesManager.getInstance(signalProcessor);
        if (instance != null) {
            OperationExecutionResult result = instance.executeOperation(arguments, operationName);
            processProcessorResult(result.getSignalProcessorData(), signalProcessor);
            return result.getDataForClient();
        }
        return null;
    }

    private void processProcessorResult(SignalProcessorData signalProcessorData, SignalProcessor signalProcessor) {
        if (signalProcessorData == null) {
            return;
        }
        threadPoolsProvider.getExecutorService().submit(() -> {
            for (ProcessingChain processingChain : signalProcessor.getProcessingChains()) {
                LinkedList<SignalProcessorData> signals = new LinkedList<>();
                signals.add(signalProcessorData);
                processingChainService.sendDataToOuputDevice(processingChain, signalProcessorData);
                processingChainService.executeProcessingChain(processingChain.getNext(), signals);
            }
        });
    }
}
