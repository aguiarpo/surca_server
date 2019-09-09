
package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Animal;
import br.org.catolicasc.surca.model.Tutor;
import br.org.catolicasc.surca.repository.AnimalRepository;
import br.org.catolicasc.surca.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1")
public class TutorEndpoint {

    private TutorRepository tutorDao;
    private AnimalRepository animalDao;

    @Autowired
    public TutorEndpoint(TutorRepository tutorDao, AnimalRepository animalDao) {
        this.tutorDao = tutorDao;
        this.animalDao = animalDao;
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
        Page<Tutor> tutors =  tutorDao.findByName(pageable, name);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/nome/like/{nome}")
    public ResponseEntity<?> getTutorByNameLike(@PathVariable("nome")String name, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/cpf/{cpf}")
    public ResponseEntity<?> getTutorByCpf(@PathVariable("cpf")String cpf, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByCpf(pageable, cpf);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/rg/{rg}")
    public ResponseEntity<?> getTutorByRg(@PathVariable("rg")String rg, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByRg(pageable, rg);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/cidade/like/{city}")
    public ResponseEntity<?> getTutorByCity(@PathVariable("city")String city, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByCityStartingWith(pageable, city);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/user/tutor/estado/{state}")
    public ResponseEntity<?> getTutorByState(@PathVariable("state")String state, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByState(pageable, state);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/tutor/criadoPor/{createdBy}")
    public ResponseEntity<?> getTutorCreatedBy(@PathVariable("createdBy")String name, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByCreatedByStartingWith(pageable, name);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/tutor/modificadoPor/{lastModifiedBy}")
    public ResponseEntity<?> getTutorLastModifiedBy(@PathVariable("lastModifiedBy")String name, Pageable pageable){
        Page<Tutor> tutors =  tutorDao.findByLastModifiedByStartingWith(pageable, name);
        return new ResponseEntity<>(tutors, HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> save(@Valid @RequestBody Tutor tutor){
        Tutor savedTutor = tutorDao.save(tutor);
        return new ResponseEntity<>(savedTutor, HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/tutor/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        List<Animal> animals = animalDao.findByTutorId(id);
        if(!animals.isEmpty())
            animalDao.deleteAll(animals);
        tutorDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> deleteAll(@RequestBody List<Tutor> tutors){
        for(Tutor tutor : tutors) {
            Long id = tutor.getId();
            List<Animal> animals = animalDao.findByTutorId(id);
            if(!animals.isEmpty())
                animalDao.deleteAll(animals);
            tutorDao.deleteById(id);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> update(@Valid @RequestBody Tutor tutor){
        return new ResponseEntity<>(tutorDao.save(tutor), HttpStatus.OK);
    }
}