
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
    private IncidentRepository incidentDao;
    private StateRepository stateDao;
    private CityRepository cityDao;
    private NeighborhoodRepository neighborhoodDao;

    @Autowired
    public TutorEndpoint(TutorRepository tutorDao, AnimalRepository animalDao, IncidentRepository incidentDao, StateRepository stateDao, CityRepository cityDao, NeighborhoodRepository neighborhoodDao) {
        this.tutorDao = tutorDao;
        this.animalDao = animalDao;
        this.incidentDao = incidentDao;
        this.stateDao = stateDao;
        this.cityDao = cityDao;
        this.neighborhoodDao = neighborhoodDao;
    }

    @PostMapping(path = "/veterinario/tutor")
    public ResponseEntity<?> save(@RequestBody Tutor tutor){
        updateOrSave(tutor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/tutor/{id}")
    public ResponseEntity<?> deleteAll(@PathVariable Long id){
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

    private void updateOrSave(Tutor tutor){
            Neighborhood neighborhood = saveAddress(tutor);
            tutor.setNeighborhood(neighborhood);
            Tutor find = tutorDao.findByCpf(tutor.getCpf());
            Tutor findRg = tutorDao.findByRg(tutor.getRg());
            for(Incident incident : tutor.getIncidents()){
                Incident findIncident = incidentDao.findByName(incident.getName());
                if(findIncident != null) incident.setCode(findIncident.getCode());
            }
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

    private Neighborhood saveAddress(Tutor tutor){
        State stateTutor = tutor.getNeighborhood().getCity().getState();
        City cityTutor = tutor.getNeighborhood().getCity();
        Neighborhood neighborhoodTutor = tutor.getNeighborhood();
        State state = stateDao.findByName(stateTutor.getName());
        if(state == null){
            State saveState = stateDao.save(stateTutor);
            cityTutor.getState().setCode(saveState.getCode());
            City saveCity = cityDao.save(cityTutor);
            neighborhoodTutor.getCity().setCode(saveCity.getCode());
            return neighborhoodDao.save(neighborhoodTutor);
        }else{
            City city = cityDao.findByNameAndStateName(cityTutor.getName(), stateTutor.getName());
            if(city == null){
                cityTutor.getState().setCode(state.getCode());
                City saveCity = cityDao.save(cityTutor);
                neighborhoodTutor.getCity().setCode(saveCity.getCode());
                return neighborhoodDao.save(neighborhoodTutor);
            } else {
                Neighborhood neighborhood = neighborhoodDao
                        .findByNameAndCity_NameAndCity_State_Name(
                                neighborhoodTutor.getName(),
                                cityTutor.getName(),
                                stateTutor.getName()
                        );
                if(neighborhood == null){
                    neighborhoodTutor.getCity().setCode(city.getCode());
                    return neighborhoodDao.save(neighborhoodTutor);
                }else{
                    return neighborhood;
                }
            }
        }
    }
}