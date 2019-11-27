package eu.wordpro.ha.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "device")
public class Device implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "slug")
    private String slug;

    @Column(name="last_sent_message")
    private String lastSentMessage;

    @Column(name="external_id")
    private String externalId;

    @ManyToOne
    @JsonIgnoreProperties("devices")
    private Space space;

    @OneToMany(mappedBy = "inputDevice")
    private Set<ProcessingChain> processingChains = new HashSet<>();


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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Device space(Space space){
        this.space = space;
        return this;
    }

    public String getLastSentMessage() {
        return lastSentMessage;
    }

    public void setLastSentMessage(String lastSentMessage) {
        this.lastSentMessage = lastSentMessage;
    }

    public Set<ProcessingChain> getProcessingChains() {
        return processingChains;
    }

    public void setProcessingChains(Set<ProcessingChain> processingChains) {
        this.processingChains = processingChains;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
