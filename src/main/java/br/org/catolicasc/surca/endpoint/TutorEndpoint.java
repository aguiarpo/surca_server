
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
public class TutorEndpoint {

    private TutorRepository tutorDao;
    private AnimalRepository animalDao;
    private VetRepository vetDao;

    @Autowired
    public TutorEndpoint(TutorRepository tutorDao, AnimalRepository animalDao, VetRepository vetDao) {
        this.tutorDao = tutorDao;
        this.animalDao = animalDao;
        this.vetDao = vetDao;
    }

    @GetMapping(path = "/user/tutor")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(tutorDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/{id}")
    public ResponseEntity<?> getTutorById(@PathVariable("id")Long id){
        Optional<Tutor> tutor =  tutorDao.findById(id);
        return new ResponseEntity<>(tutor, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/nome/{nome}")
    public ResponseEntity<?> getTutorByName(@PathVariable("nome")String name, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByNameAndStatus(pageable, name, Status.VISIBLE);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/nome/like/{nome}")
    public ResponseEntity<?> getTutorByNameLike(@PathVariable("nome")String name, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByNameStartingWithAndStatus(pageable, name, Status.VISIBLE);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/cpf/{cpf}")
    public ResponseEntity<?> getTutorByCpf(@PathVariable("cpf")String cpf){
        Tutor tutors =  tutorDao.findByCpfAndStatus(cpf, Status.VISIBLE);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/rg/{rg}")
    public ResponseEntity<?> getTutorByRg(@PathVariable("rg")String rg, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByRgAndStatus(pageable, rg, Status.VISIBLE);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> save(@RequestBody TutorWithAnimals tutor){
        Tutor findTutor = tutorDao.findByCpf(tutor.getTutor().getCpf());
        if(findTutor != null)
            tutor.getTutor().setCode(findTutor.getCode());
        List<Animal> animals = tutor.getAnimals();
        Tutor savedTutor = tutorDao.save(tutor.getTutor());
        if(animals != null) {
            for (Animal animal : animals) {
                animal.setTutor(savedTutor);
                animal.setCastrator(findByCrmv(animal.getCastrator().getCrmv()));
                animal.setVetMicrochip(findByCrmv(animal.getVetMicrochip().getCrmv()));
                animalDao.save(animal);
            }
        }
        return new ResponseEntity<>(savedTutor, HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/tutor/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Tutor> tutor = tutorDao.findById(id);
        List<Animal> animals = animalDao.findByTutorCode(id);
        if(!animals.isEmpty()) {
            for (Animal animal : animals) {
                animal.setStatus(Status.INVISIBLE);
                animalDao.save(animal);
            }
        }
        tutor.get().setStatus(Status.INVISIBLE);
        tutorDao.save(tutor.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> deleteAll(@RequestBody List<Tutor> tutors){
        for(Tutor tutor : tutors) {
            Long id = tutor.getCode();
            Optional<Tutor> findTutor = tutorDao.findById(id);
            List<Animal> animals = animalDao.findByTutorCode(id);
            if(!animals.isEmpty())
                for (Animal animal : animals){
                    animal.setStatus(Status.INVISIBLE);
                    animalDao.save(animal);
                }
            findTutor.get().setStatus(Status.INVISIBLE);
            tutorDao.save(findTutor.get());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> update(@RequestBody Tutor tutor){
        return new ResponseEntity<>(tutorDao.save(tutor), HttpStatus.OK);
    }

    private Vet findByCrmv(String crmv){
        return vetDao.findByCrmv(crmv);
    }
}