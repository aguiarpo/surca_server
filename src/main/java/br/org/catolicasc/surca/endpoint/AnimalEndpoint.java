package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.AnimalRepository;
import br.org.catolicasc.surca.repository.TutorRepository;
import br.org.catolicasc.surca.repository.VetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class AnimalEndpoint {

    private AnimalRepository animalDao;
    private TutorRepository tutorDao;
    private VetRepository vetDao;

    @Autowired
    public AnimalEndpoint(AnimalRepository animalDao, TutorRepository tutorDao, VetRepository vetDao) {
        this.animalDao = animalDao;
        this.tutorDao = tutorDao;
        this.vetDao = vetDao;
    }

    @DeleteMapping(path = "/veterinario/animal/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Long tutorId = animalDao.findIdByIdTutor(id);
        Optional<Animal> animal = animalDao.findById(id);
        if(animal.isPresent()) {
            animal.get().setStatus(Status.INVISIBLE);
            animalDao.save(animal.get());
            if (animalDao.findByTutorWithAnimal(tutorId, Status.INVISIBLE) == 0) {
                Optional<Tutor> tutor = tutorDao.findById(tutorId);
                tutor.get().setStatus(Status.INVISIBLE);
                tutorDao.save(tutor.get());
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/animal")
    public ResponseEntity<?> deleteAll(@RequestBody List<Animal> animals){
        for(Animal animal : animals) {
            Long id = animal.getCode();
            Long tutorId = animalDao.findIdByIdTutor(id);
            Optional<Animal> findAnimal = animalDao.findById(animal.getCode());
            if(findAnimal.isPresent()) {
                animal = findAnimal.get();
                animal.setStatus(Status.INVISIBLE);
                animalDao.save(animal);
                if (animalDao.findByTutorWithAnimal(tutorId, Status.INVISIBLE) == 0) {
                    Optional<Tutor> tutor = tutorDao.findById(tutorId);
                    tutor.get().setStatus(Status.INVISIBLE);
                    tutorDao.save(tutor.get());
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/veterinario/animal")
    public ResponseEntity<?> update(@RequestBody TutorWithAnimals tutorWithAnimals){
        for(Animal animal : tutorWithAnimals.getAnimals()) {
            animal.setCastrator(findByCrmv(animal.getCastrator().getCrmv()));
            animal.setVetMicrochip(findByCrmv(animal.getVetMicrochip().getCrmv()));
            Tutor tutor = tutorDao.findByCpf(animal.getTutor().getCpf());
            animal.setTutor(tutor);
            return new ResponseEntity<>(animalDao.save(animal), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Vet findByCrmv(String crmv){
        return vetDao.findByCrmv(crmv);
    }
}