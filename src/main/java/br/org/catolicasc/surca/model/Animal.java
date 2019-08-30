package br.org.catolicasc.surca.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;

@Entity
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Integer microchipNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private String species;
    @Temporal(TemporalType.DATE)
    @Past
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull
    private Date birthDate;
    @NotNull
    private Boolean animalCastrated;
    @NotEmpty
    private String coatColor;
    @NotEmpty
    private String breed;
    @NotNull
    private Short sizeCm;
    @Temporal(TemporalType.DATE)
    @Past
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull
    private Date dateMicrochip;
    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToOne(cascade = CascadeType.ALL)
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    public Date getDateMicrochip() {
        return dateMicrochip;
    }

    public void setDateMicrochip(Date dateMicrochip) {
        this.dateMicrochip = dateMicrochip;
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
}
