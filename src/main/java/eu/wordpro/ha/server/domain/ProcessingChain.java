package eu.wordpro.ha.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "processing_chain")
public class ProcessingChain {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JsonIgnoreProperties("processingChains")
    private Device inputDevice;

    @ManyToOne
    private Device outputDevice;

    @ManyToOne
    private SignalProcessor signalProcessor;

    @ManyToOne
    private ProcessingChain next;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getInputDevice() {
        return inputDevice;
    }

    public void setInputDevice(Device inputDevice) {
        this.inputDevice = inputDevice;
    }

    public Device getOutputDevice() {
        return outputDevice;
    }

    public void setOutputDevice(Device outputDevice) {
        this.outputDevice = outputDevice;
    }

    public SignalProcessor getSignalProcessor() {
        return signalProcessor;
    }

    public void setSignalProcessor(SignalProcessor signalProcessor) {
        this.signalProcessor = signalProcessor;
    }

    public ProcessingChain getNext() {
        return next;
    }

    public void setNext(ProcessingChain next) {
        this.next = next;
    }
}
