package br.org.catolicasc.surca.model;


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
    private String incident;

    @Column(columnDefinition = "TEXT")
    private String comments;

    @ManyToMany
    @JoinTable(name="tutor_incident", joinColumns=
            {@JoinColumn(name="incident_id")}, inverseJoinColumns=
            {@JoinColumn(name="tutor_id")})
    private List<Tutor> tutors;
}
