package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.*;
import br.org.catolicasc.surca.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1")
public class AllEndpoint{

    private MedicationsRepository medicationsDao;
    private UserRepository userDao;
    private TutorRepository tutorDao;
    private IncidentRepository incidentDao;
    private AnimalRepository animalDao;
    private VetRepository vetDao;
    private StateRepository stateDao;
    private CityRepository cityDao;
    private NeighborhoodRepository neighborhoodDao;

    public AllEndpoint(MedicationsRepository medicationsDao, UserRepository userDao, TutorRepository tutorDao, IncidentRepository incidentDao, AnimalRepository animalDao, VetRepository vetDao, StateRepository stateDao, CityRepository cityDao, NeighborhoodRepository neighborhoodDao) {
        this.medicationsDao = medicationsDao;
        this.userDao = userDao;
        this.tutorDao = tutorDao;
        this.incidentDao = incidentDao;
        this.animalDao = animalDao;
        this.vetDao = vetDao;
        this.stateDao = stateDao;
        this.cityDao = cityDao;
        this.neighborhoodDao = neighborhoodDao;
    }

    @Autowired


    @GetMapping(path = "/user/all")
    public ResponseEntity<?> listAll(){
        Map<String, Iterable> map = new HashMap<String, Iterable>();
        map.put("medications", medicationsDao.findAll());
        map.put("tutors", tutorDao.findAll());
        map.put("animals", animalDao.findAll());
        map.put("incidents", incidentDao.findAll());
        map.put("states", stateDao.findAll());
        map.put("cities", cityDao.findAll());
        map.put("neighborhood", neighborhoodDao.findAll());
        Iterable<Vet> vets = vetDao.findAll();
        List listVets = new ArrayList();
        for (Vet vet: vets) {
            vet.getUser().setPassword(null);
            listVets.add(vet);
        }
        map.put("vets", listVets);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping(path = "/admin/all")
    public ResponseEntity<?> listAll2(){
        Map<String, Iterable> map = new HashMap<String, Iterable>();
        map.put("medications", medicationsDao.findAll());
        map.put("tutors", tutorDao.findAll());
        map.put("animals", animalDao.findAll());
        map.put("incidents", incidentDao.findAll());
        map.put("vets", vetDao.findAll());
        map.put("states", stateDao.findAll());
        map.put("cities", cityDao.findAll());
        map.put("neighborhood", neighborhoodDao.findAll());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}