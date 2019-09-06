package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@SequenceGenerator(name = "vaccine_seq", sequenceName = "vaccine_seq",
        initialValue = 2, allocationSize = 1)
public class Vaccine extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vaccine_seq")
    private Long id;
    @Column(unique = true)
    @NotEmpty
    private String name;

    @ManyToMany(mappedBy="vaccines", cascade = CascadeType.REMOVE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Animal> animals;

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

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
