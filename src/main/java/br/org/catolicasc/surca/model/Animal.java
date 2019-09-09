package br.org.catolicasc.surca.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "animal_seq", sequenceName = "animal_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_name", columnList="name"),
                @Index(name = "index_species", columnList="species"),
                @Index(name = "index_birth_date", columnList="birthDate"),
                @Index(name = "index_breed", columnList="breed"),
                @Index(name = "index_date_microchip", columnList="dateMicrochip")})
public class Animal extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_seq")
    private Long id;
    @Column(unique = true)
    @NotNull
    @Size(max = 20)
    private String microchipNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private String species;
    @Past
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private Boolean animalCastrated;
    @NotEmpty
    private String coatColor;
    @NotEmpty
    private String breed;
    @NotNull
    private Short sizeCm;
    @Past
    @NotNull
    private LocalDate dateMicrochip;
    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Tutor tutor;

    @ManyToOne
    private Vet vetMicrochip;

    @ManyToOne
    private Vet castrator;

    @ManyToMany
    @JoinTable(name="animal_vaccines", joinColumns=
            {@JoinColumn(name="animal_id")}, inverseJoinColumns=
            {@JoinColumn(name="vaccine_id")})
    private List<Vaccine> vaccines;

    @ManyToMany
    @JoinTable(name="animal_medications", joinColumns=
            {@JoinColumn(name="animal_id")}, inverseJoinColumns=
            {@JoinColumn(name="medications_id")})
    private List<Medications> medications;
}
