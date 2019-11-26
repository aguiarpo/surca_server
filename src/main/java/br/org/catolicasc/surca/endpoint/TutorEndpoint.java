
package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IncidentRepository incidentDao;
    private AnimalMedicationsRepository animalMedicationsDao;

    @Autowired
    public TutorEndpoint(TutorRepository tutorDao, AnimalRepository animalDao, VetRepository vetDao, IncidentRepository incidentDao, AnimalMedicationsRepository animalMedicationsDao) {
        this.tutorDao = tutorDao;
        this.animalDao = animalDao;
        this.vetDao = vetDao;
        this.incidentDao = incidentDao;
        this.animalMedicationsDao = animalMedicationsDao;
    }

    @PostMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> save(@RequestBody TutorWithAnimals tutor){
        saveIncident(tutor.getTutor().getIncidents());
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
                Animal savedAnimal = animalDao.save(animal);
                for(AnimalMedications medication : animal.getMedications()){
                    medication.setAnimal(savedAnimal);
                    animalMedicationsDao.save(medication);
                }
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

    private void saveIncident(List<Incident>  incidents){
        for(Incident incident : incidents){
            if(incident.getName() != null)
                incidentDao.save(incident);
        }
    }
}