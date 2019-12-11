package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.*;
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
    private AnimalMedicationsRepository animalMedicationsDao;
    private MedicationsRepository medicationsDao;
    private NeighborhoodRepository neighborhoodDao;

    @Autowired
    public AnimalEndpoint(NeighborhoodRepository neighborhoodDao, AnimalRepository animalDao,MedicationsRepository medicationsDao, TutorRepository tutorDao, VetRepository vetDao, AnimalMedicationsRepository animalMedicationsDao) {
        this.animalDao = animalDao;
        this.tutorDao = tutorDao;
        this.vetDao = vetDao;
        this.neighborhoodDao = neighborhoodDao;
        this.animalMedicationsDao = animalMedicationsDao;
        this.medicationsDao = medicationsDao;
    }

    @DeleteMapping(path = "/veterinario/animal/{id}")
    public ResponseEntity<?> deleteAll(@PathVariable Long id){
            Animal animal;
            Long tutorId = animalDao.findIdByIdTutor(id);
            Optional<Animal> findAnimal = animalDao.findById(id);
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/admin/animal/report/{neighborhood}")
    public ResponseEntity<?> getNeighborhood(@PathVariable String neighborhood){

        return new ResponseEntity<>(animalDao.findByNeighborhood(neighborhood),
                HttpStatus.OK);
    }

    @GetMapping(path = "/admin/animal/{neighborhood}")
    public ResponseEntity<?> getNeighborhoodLike(@PathVariable String neighborhood, Pageable pageable){

        return new ResponseEntity<>(neighborhoodDao.findByNameLike(neighborhood, pageable),
                HttpStatus.OK);
    }

    @GetMapping(path = "/admin/animal/report")
    public ResponseEntity<?> getAllNeighborhood(){

        return new ResponseEntity<>(animalDao.findByAllNeighborhood(),
                HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/animal")
    public ResponseEntity<?> save(@RequestBody Animal animal){
        updateOrSave(animal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/animal")
    public ResponseEntity<?> update(@RequestBody Animal animal){
        updateOrSave(animal);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateOrSave(Animal animal){
            Vet vet = vetDao.findByCrmv(animal.getVetMicrochip().getCrmv());
            Tutor tutor = tutorDao.findByCpf(animal.getTutor().getCpf());
            Animal savedAnimal;
            if(vet != null && tutor != null){
                animal.getTutor().setCode(tutor.getCode());
                animal.getVetMicrochip().setCode(vet.getCode());
                Animal find = animalDao.findByMicrochipNumber(animal.getMicrochipNumber());
                if(find == null){
                    savedAnimal = animalDao.save(animal);
                }
                else{
                    animal.setCode(find.getCode());
                    savedAnimal = animalDao.save(animal);
                }
                if(savedAnimal != null){
                    saveMedications(animal.getMedications(), savedAnimal);
                }
            }
    }

    private void saveMedications(List<AnimalMedications> medications, Animal animal){
        animalMedicationsDao.deleteByAnimalCode(animal.getCode());
        for(AnimalMedications medication : medications){
            if(medication.getMedication().getName() != null && medication.getDateMedications() != null){
                Medications find = medicationsDao.findByName(medication.getMedication().getName());
                if(find != null){
                    medication.setAnimal(animal);
                    medication.getMedication().setCode(find.getCode());
                    animalMedicationsDao.save(medication);
                }
            }
        }
    }
}