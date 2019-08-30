package br.org.catolicasc.surca.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Vet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotNull
    private Long crmv;
    @NotEmpty
    private String state;
    @NotEmpty
    private String city;
    @Column(unique = true)
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "vetMicrochip")
    private List<Animal> animalsMicrochip;

    @OneToMany(mappedBy = "castrator")
    private List<Animal> animalsCastrator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCrmv() {
        return crmv;
    }

    public void setCrmv(Long crmv) {
        this.crmv = crmv;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Animal> getAnimalsMicrochip() {
        return animalsMicrochip;
    }

    public void setAnimalsMicrochip(List<Animal> animalsMicrochip) {
        this.animalsMicrochip = animalsMicrochip;
    }

    public List<Animal> getAnimalsCastrator() {
        return animalsCastrator;
    }

    public void setAnimalsCastrator(List<Animal> animalsCastrator) {
        this.animalsCastrator = animalsCastrator;
    }
}
