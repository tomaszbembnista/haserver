package eu.wordpro.ha.server.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "signal_processor")
public class SignalProcessor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "class_name")
    private String className;

    @Column(name="status")
    private String status;

    @Column(name="configuration")
    private String configuration;

    @Column(name="state")
    private String state;

    @OneToMany(mappedBy = "signalProcessor")
    private Set<ProcessingChain> processingChains = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "space_id")
    private Space space;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<ProcessingChain> getProcessingChains() {
        return processingChains;
    }

    public void setProcessingChains(Set<ProcessingChain> processingChains) {
        this.processingChains = processingChains;
    }

    public Space getSpace() { return space; }

    public void setSpace(Space space) { this.space = space; }

    @Override
    public String toString() {
        return "SignalProcessor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", status='" + status + '\'' +
                ", configuration='" + configuration + '\'' +
                ", state='" + state + '\'' +
                ", processingChains=" + processingChains +
                '}';
    }
}
