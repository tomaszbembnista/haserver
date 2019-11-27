package pl.pbaranowski.ha.heatingplan.logic;

import com.google.gson.Gson;
import eu.wordpro.ha.api.*;
import eu.wordpro.ha.api.model.ProcessorOperationArgument;
import eu.wordpro.ha.api.model.ProcessorOperationDesc;
import eu.wordpro.ha.api.model.StringSignalProcessorData;
import pl.pbaranowski.ha.heatingplan.model.HeatingPlan;
import pl.pbaranowski.ha.heatingplan.model.Input;
import pl.pbaranowski.ha.heatingplan.model.Output;

import java.util.LinkedList;
import java.util.List;

public class HeatingService implements SignalProcessor {

    private HeatingPlan heatingPlan;
    private Gson gson = new Gson();

    @Override
    public void setConfiguration(String configuration) throws InvalidConfigurationException {
        Gson gson = new Gson();
        try {
            heatingPlan = gson.fromJson(configuration, HeatingPlan.class);
        }catch(Exception e){
            throw new InvalidConfigurationException("Wrong JSON syntax");
        }
        if (heatingPlan == null) {
            throw new InvalidConfigurationException("JSON could not be deserialized to heating plan.");
        }
    }

    @Override
    public void setState(String state) throws InvalidStateException {

    }

    @Override
    public String getState() {
        return null;
    }

    @Override
    public SignalProcessorData processInput(LinkedList<SignalProcessorData> inputs) throws InvalidSignalException {
        Input input = getInput(inputs);
        Output output = getOutput(System.currentTimeMillis(), input.tmp);
        return prepareOutput(output);
    }

    public Output getOutput(long currentTime, double currentTemp){
        //find temperature for the given time
        //here only sample logic basing on default tmp
        double temperatureToKeep =  heatingPlan.defaultTemp;
        Output output = new Output();
        //temp to low
        if (currentTemp < temperatureToKeep - heatingPlan.hysteresis){
            output.switchOn = true;
        } else if (currentTemp > temperatureToKeep + heatingPlan.hysteresis){
            output.switchOff = true;
        }
        return output;
    }

    @Override
    public List<ProcessorOperationDesc> listPossibleOperations() {
        return null;
    }

    @Override
    public String executeOperation(List<ProcessorOperationArgument> arguments, String name) {
        return null;
    }

    private Input getInput(LinkedList<SignalProcessorData> inputs) throws InvalidSignalException {
        Input input;
        try {
            StringSignalProcessorData processorData = (StringSignalProcessorData) inputs.getLast();
            String data = processorData.getData();
            input = gson.fromJson(data, Input.class);
        }
        catch (Exception e){
            throw new InvalidSignalException(e.getMessage());
        }
        return input;
    }

    private SignalProcessorData prepareOutput(Output output){
        String outputAsJson = gson.toJson(output);
        SignalProcessorData result = new StringSignalProcessorData(outputAsJson);
        return result;
    }
}
