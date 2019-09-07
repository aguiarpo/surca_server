package br.org.catolicasc.surca.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "vet_seq", sequenceName = "vet_seq",
        initialValue = 2, allocationSize = 1)
public class Vet{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vet_seq")
    private Long id;
    @Column(unique = true)
    @NotNull
    @Size(max = 20)
    private String crmv;
    @OneToOne(cascade = CascadeType.ALL)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "vetMicrochip")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Animal> animalsMicrochip;

    @OneToMany(mappedBy = "castrator")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Animal> animalsCastrator;

}
