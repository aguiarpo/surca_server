package br.org.catolicasc.surca.model;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(mappedBy = "vetMicrochip")
    private List<Animal> animalsMicrochip;

    @OneToMany(mappedBy = "castrator")
    private List<Animal> animalsCastrator;

    private @CreatedDate
    LocalDateTime createdDate;
    private @LastModifiedDate
    LocalDateTime lastModifiedDate;

    private @CreatedBy
    String createdBy;
    private @LastModifiedBy
    String lastModifiedBy;

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
