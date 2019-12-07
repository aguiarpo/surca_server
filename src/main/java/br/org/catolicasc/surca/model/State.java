package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "state_seq", sequenceName = "state_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_name", columnList="name")},
        uniqueConstraints = @UniqueConstraint(name = "name", columnNames=  "name" ))
public class State{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "state_seq")
    @Column(name = "id")
    private Long code;

    @NotEmpty
    @Size(min = 2, max = 2)
    private String name;

    @OneToMany(mappedBy = "state")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private List<City> city;
}
