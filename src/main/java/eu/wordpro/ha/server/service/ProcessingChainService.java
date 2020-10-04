package eu.wordpro.ha.server.service;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.api.model.ProcessingData;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;

import java.util.LinkedList;
import java.util.List;

public interface ProcessingChainService extends Service<ProcessingChainDTO> {
    void executeProcessingChain(ProcessingChain processingChain, ProcessingData data);

    void sendDataToOuputDevice(ProcessingChain processingChain, SignalProcessorData result);

    List<ProcessingChainDTO> findProcessingChainsByInputDevice(Long deviceId);

    List<ProcessingChainDTO> findAllStepsOfProcessingChain(Long rootId);
}
