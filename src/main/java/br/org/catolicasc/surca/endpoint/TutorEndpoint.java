
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
    public ResponseEntity<?> save(@RequestBody Tutor tutor){
        updateOrSave(tutor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/tutor/remover")
    public ResponseEntity<?> deleteAll(@RequestBody Tutor tutor){
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> update(@RequestBody Tutor tutor){
        updateOrSave(tutor);
        return new ResponseEntity<>(HttpStatus.OK);
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

    private void updateOrSave(Tutor tutor){
            Tutor find = tutorDao.findByCpf(tutor.getCpf());
            Tutor findRg = tutorDao.findByRg(tutor.getRg());
            if(find == null && findRg == null)tutorDao.save(tutor);
            else{
                if(find != null){
                    tutor.setCode(find.getCode());
                    tutorDao.save(tutor);
                }else{
                    tutor.setCode(findRg.getCode());
                    tutorDao.save(tutor);
                }
            }
    }
}