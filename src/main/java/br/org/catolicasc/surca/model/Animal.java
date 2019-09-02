package br.org.catolicasc.surca.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Animal extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Integer microchipNumber;
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="animal_vaccines", joinColumns=
            {@JoinColumn(name="animal_id")}, inverseJoinColumns=
            {@JoinColumn(name="vaccine_id")})
    private List<Vaccine> vaccines;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="animal_medications", joinColumns=
            {@JoinColumn(name="animal_id")}, inverseJoinColumns=
            {@JoinColumn(name="medications_id")})
    private List<Medications> medications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMicrochipNumber() {
        return microchipNumber;
    }

    public void setMicrochipNumber(Integer microchipNumber) {
        this.microchipNumber = microchipNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public Boolean getAnimalCastrated() {
        return animalCastrated;
    }

    public void setAnimalCastrated(Boolean animalCastrated) {
        this.animalCastrated = animalCastrated;
    }

    public String getCoatColor() {
        return coatColor;
    }

    public void setCoatColor(String coatColor) {
        this.coatColor = coatColor;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Short getSizeCm() {
        return sizeCm;
    }

    public void setSizeCm(Short sizeCm) {
        this.sizeCm = sizeCm;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Vet getVetMicrochip() {
        return vetMicrochip;
    }

    public void setVetMicrochip(Vet vetMicrochip) {
        this.vetMicrochip = vetMicrochip;
    }

    public Vet getCastrator() {
        return castrator;
    }

    public void setCastrator(Vet castrator) {
        this.castrator = castrator;
    }

    public List<Vaccine> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<Vaccine> vaccines) {
        this.vaccines = vaccines;
    }

    public List<Medications> getMedications() {
        return medications;
    }

    public void setMedications(List<Medications> medications) {
        this.medications = medications;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateMicrochip() {
        return dateMicrochip;
    }

    public void setDateMicrochip(LocalDate dateMicrochip) {
        this.dateMicrochip = dateMicrochip;
    }
}
