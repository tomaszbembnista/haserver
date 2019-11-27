package eu.wordpro.ha.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="space")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "hibernate_sequence", allocationSize = 1)
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @OneToMany(mappedBy = "parent")
    private Set<Space> subspaces = new HashSet<>();

    @OneToMany(mappedBy = "space")
    private Set<Device> devices = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("subspaces")
    private Space parent;

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

    public Set<Space> getSubspaces() {
        return subspaces;
    }

    public void setSubspaces(Set<Space> subspaces) {
        this.subspaces = subspaces;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public Space getParent() {
        return parent;
    }

    public void setParent(Space parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Space space = (Space) o;

        if (id != null ? !id.equals(space.id) : space.id != null) return false;
        if (!name.equals(space.name)) return false;
        if (slug != null ? !slug.equals(space.slug) : space.slug != null) return false;
        if (subspaces != null ? !subspaces.equals(space.subspaces) : space.subspaces != null) return false;
        if (devices != null ? !devices.equals(space.devices) : space.devices != null) return false;
        return parent != null ? parent.equals(space.parent) : space.parent == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + (slug != null ? slug.hashCode() : 0);
        result = 31 * result + (subspaces != null ? subspaces.hashCode() : 0);
        result = 31 * result + (devices != null ? devices.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Space{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", subspaces=" + subspaces +
                ", devices=" + devices +
                ", parent=" + parent +
                '}';
    }
}
