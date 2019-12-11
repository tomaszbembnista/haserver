package eu.wordpro.ha.server.service;

import eu.wordpro.ha.api.SignalProcessorData;
import eu.wordpro.ha.server.domain.ProcessingChain;
import eu.wordpro.ha.server.service.dto.ProcessingChainDTO;

import java.util.LinkedList;

public interface ProcessingChainService extends Service<ProcessingChainDTO> {
    void executeProcessingChain(ProcessingChain processingChain, LinkedList<SignalProcessorData> inputs);

    void sendDataToOuputDevice(ProcessingChain processingChain, SignalProcessorData result);
}
