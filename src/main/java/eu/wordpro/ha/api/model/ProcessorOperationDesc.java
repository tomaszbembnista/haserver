package eu.wordpro.ha.api.model;

import java.util.List;

public class ProcessorOperationDesc {

    private String name;
    private List<ProcessorOperationArgumentDesc> arguments;
    private DataType result;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProcessorOperationArgumentDesc> getArguments() {
        return arguments;
    }

    public void setArguments(List<ProcessorOperationArgumentDesc> arguments) {
        this.arguments = arguments;
    }

    public DataType getResult() {
        return result;
    }

    public void setResult(DataType result) {
        this.result = result;
    }
}
