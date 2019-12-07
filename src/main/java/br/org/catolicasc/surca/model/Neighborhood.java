package br.org.catolicasc.surca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Getter
@Setter
@SequenceGenerator(name = "neighborhood_seq", sequenceName = "neighborhood_seq",
        initialValue = 2, allocationSize = 1)
@Table(indexes = {@Index(name = "index_name", columnList="name")})
public class Neighborhood {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "neighborhood_seq")
    @Column(name = "id")
    private Long code;

    @NotEmpty
    private String name;

    @ManyToOne
    private City city;

    @OneToMany(mappedBy = "neighborhood")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    private List<Tutor> tutor;
}
