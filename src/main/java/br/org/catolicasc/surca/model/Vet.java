package br.org.catolicasc.surca.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Vet extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotNull
    @Size(max = 20)
    private String crmv;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
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

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
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
