package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "tutor_seq", sequenceName = "tutor_seq",
        initialValue = 2, allocationSize = 1)
public class Tutor extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tutor_seq")
    private Long id;
    @Column(unique = true)
    @CPF
    @NotEmpty
    @Size(min = 14, max = 14)
    private String cpf;
    @Column(unique = true)
    @NotEmpty
    @Size(max = 10)
    private String rg;
    @NotEmpty
    private String motherName;
    @NotEmpty
    private String name;
    @NotEmpty
    private String street;
    private Short number;
    @NotEmpty
    private String neighborhood;
    private String complement;
    @NotEmpty
    private String city;
    @Size(min = 2, max = 2)
    @NotEmpty
    private String state;
    @NotEmpty
    @Size(max = 9)
    private String cep;
    @NotEmpty
    private String profession;
    @NotNull
    @Size(max = 15)
    private String telephone1;
    @Size(max = 15)
    private String telephone2;

    @OneToMany(mappedBy = "tutor")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Animal> animals;
}
