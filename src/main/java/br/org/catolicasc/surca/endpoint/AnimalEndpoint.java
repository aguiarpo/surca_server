package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Animal;
import br.org.catolicasc.surca.model.Tutor;
import br.org.catolicasc.surca.model.Vet;
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

    @GetMapping(path = "/user/animal")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(animalDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/{id}")
    public ResponseEntity<?> getAnimalById(@PathVariable("id")Long id){
        Optional<Animal> animal =  animalDao.findById(id);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/{microchipNumber}")
    public ResponseEntity<?> getAnimalByMicrochipNumber(@PathVariable("microchipNumber")String microchipNumber){
        Animal animal =  animalDao.findByMicrochipNumber(microchipNumber);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/nome/{nome}")
    public ResponseEntity<?> getAnimalByName(@PathVariable("nome")String name, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findByName(pageable, name);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/nome/like/{nome}")
    public ResponseEntity<?> getAnimalByNameLike(@PathVariable("nome")String name, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/especie/{especie}")
    public ResponseEntity<?> getAnimalBySpecies(@PathVariable("especie")String species, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findBySpecies(pageable, species);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/raca/{raca}")
    public ResponseEntity<?> getAnimalByBreed(@PathVariable("raca")String breed, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findByBreed(pageable, breed);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataMicrochip/{ano}")
    public ResponseEntity<?> getAnimalByDateMicrochipAge(@PathVariable("ano") int
                                                                 age, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findDateMicrochipByAge(pageable, age);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataMicrochip/{ano}/{mes}")
    public ResponseEntity<?> getAnimalByDateMicrochipMouth(@PathVariable("ano") int age, @PathVariable("mes") int
            month, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findDateMicrochipByMouth(pageable, age, month);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataMicrochip/{ano}/{mes}/{dia}")
    public ResponseEntity<?> getAnimalByDateMicrochip(@PathVariable("ano") int age,
                                                      @PathVariable("mes") int month, @PathVariable("dia") int day, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findByDateMicrochip(pageable, age, month, day);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataNascimento/{ano}")
    public ResponseEntity<?> getAnimalByBirthDateAge(@PathVariable("ano") int
                                                             age, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findBirthDateByAge(pageable, age);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataNascimento/{ano}/{mes}")
    public ResponseEntity<?> getAnimalByBirthDateMouth(@PathVariable("ano") int age, @PathVariable("mes") int
            month, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findBirthDateByMouth(pageable, age, month);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @GetMapping(path = "/user/animal/dataNascimento/{ano}/{mes}/{dia}")
    public ResponseEntity<?> getAnimalByBirthDate(@PathVariable("ano") int age,
                                                  @PathVariable("mes") int month, @PathVariable("dia") int day, Pageable pageable){
        Page<Animal> animalModels =  animalDao.findByBirthDate(pageable, age, month, day);
        return new ResponseEntity<>(animalModels, HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/animal")
    public ResponseEntity<?> save(@RequestBody Animal animal){
        Tutor tutor = tutorDao.findByCpf(animal.getTutor().getCpf());
        animal.setCastrator(findByCrmv(animal.getCastrator().getCrmv()));
        animal.setVetMicrochip(findByCrmv(animal.getVetMicrochip().getCrmv()));
        if(tutor == null){
            tutor = tutorDao.save(animal.getTutor());
            animal.setTutor(tutor);
        }else{
            animal.setTutor(tutor);
        }
        return new ResponseEntity<>(animalDao.save(animal), HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/animal/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        Long tutorId = animalDao.findIdByIdTutor(id);
        animalDao.deleteById(id);
        if(animalDao.findByTutorWithAnimal(tutorId) == 0){
            tutorDao.deleteById(tutorId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/animal")
    public ResponseEntity<?> deleteAll(@RequestBody List<Animal> animals){
        for(Animal animal : animals) {
            Long id = animal.getId();
            Long tutorId = animalDao.findIdByIdTutor(id);
            animalDao.deleteById(id);
            if(animalDao.findByTutorWithAnimal(tutorId) == 0){
                tutorDao.deleteById(tutorId);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/veterinario/animal")
    public ResponseEntity<?> update(@RequestBody Animal animal){
        animal.setCastrator(findByCrmv(animal.getCastrator().getCrmv()));
        animal.setVetMicrochip(findByCrmv(animal.getVetMicrochip().getCrmv()));
        Tutor tutor = tutorDao.findByCpf(animal.getTutor().getCpf());
        animal.getTutor().setId(tutor.getId());
        return new ResponseEntity<>(animalDao.save(animal), HttpStatus.OK);
    }

    private Vet findByCrmv(String crmv){
        return vetDao.findByCrmv(crmv);
    }
}