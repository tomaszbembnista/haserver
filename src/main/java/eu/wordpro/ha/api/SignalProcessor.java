package eu.wordpro.ha.api;

import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;

import java.util.LinkedList;
import java.util.List;

public interface SignalProcessor
{
    void setConfiguration(String configuration) throws InvalidConfigurationException;

    void setState(String state) throws InvalidStateException;

    String getState();

    SignalProcessorData processInput(LinkedList<SignalProcessorData> inputs) throws InvalidSignalException;

    List<ProcessorOperationDesc> listPossibleOperations();

    String executeOperation(List<ProcessorOperationArgument> arguments, String name);

}
