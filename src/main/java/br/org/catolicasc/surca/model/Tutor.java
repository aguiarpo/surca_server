package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.rest.core.annotation.RestResource;

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
@Table(indexes = {@Index(name = "index_name", columnList="name"),
        @Index(name = "index_mother_name", columnList="motherName"),
        @Index(name = "index_city", columnList="city"),
        @Index(name = "index_state", columnList="state"),
        @Index(name = "index_created_by", columnList="createdBy"),
        @Index(name = "index_last_modified_by", columnList="lastModifiedBy")},
        uniqueConstraints = {@UniqueConstraint(name = "cpf", columnNames=  "cpf" ), @UniqueConstraint(name = "rg", columnNames = "rg")})
public class Tutor extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tutor_seq")
    @Column(name = "id")
    private Long code;
    @CPF
    @NotEmpty
    @Size(min = 14, max = 14)
    private String cpf;
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

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.VISIBLE;

    @OneToMany(mappedBy = "tutor")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private List<Animal> animals;
}
