package eu.wordpro.ha.server.service.dto;

import eu.wordpro.ha.server.domain.Device;

public class ProcessingChainDTO {

    private Long id;

    private Long inputDeviceId;

    private Long outputDeviceId;

    private Long signalProcessorId;

    private Long nextId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInputDeviceId() {
        return inputDeviceId;
    }

    public void setInputDeviceId(Long inputDeviceId) {
        this.inputDeviceId = inputDeviceId;
    }

    public Long getOutputDeviceId() {
        return outputDeviceId;
    }

    public void setOutputDeviceId(Long outputDeviceId) {
        this.outputDeviceId = outputDeviceId;
    }

    public Long getSignalProcessorId() {
        return signalProcessorId;
    }

    public void setSignalProcessorId(Long signalProcessorId) {
        this.signalProcessorId = signalProcessorId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }
}
