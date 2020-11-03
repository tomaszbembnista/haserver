package eu.wordpro.ha.server.service;

import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.server.service.dto.SignalProcessorDTO;

import java.util.List;

public interface SignalProcessorService extends Service<SignalProcessorDTO> {
    List<ProcessorOperationDesc> getPossibleOperations(Long processorId);
    String executeOperation(Long processorId, String operationName, List<ProcessorOperationArgument> arguments);
    List<SignalProcessorDTO> findSignalProcessorsInSpace(Long id);
}
