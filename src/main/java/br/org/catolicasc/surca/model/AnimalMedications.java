package br.org.catolicasc.surca.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@SequenceGenerator(name = "animal_medications_seq", sequenceName = "animal_medications_seq",
        initialValue = 2, allocationSize = 1)
public class AnimalMedications {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "animal_medications_seq")
    @Column(name = "id")
    private Long code;

    private LocalDate dateMedications;

    @ManyToOne
    @JoinColumn
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Animal animal;


    @ManyToOne
    @JoinColumn
    private Medications medication;
}
