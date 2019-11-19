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
@SequenceGenerator(name = "incident_seq", sequenceName = "incident_seq",
        initialValue = 2, allocationSize = 1)
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incident_seq")
    @Column(name = "id")
    private Long code;

    @NotEmpty
    private String name;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToMany
    @JoinTable(name="tutor_incident", joinColumns=
            {@JoinColumn(name="incident_id")}, inverseJoinColumns=
            {@JoinColumn(name="tutor_id")})
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Tutor> tutors;
}
