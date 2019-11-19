package br.org.catolicasc.surca.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TutorWithAnimals {
    private List<Animal> animals;
    private Tutor tutor;
}
