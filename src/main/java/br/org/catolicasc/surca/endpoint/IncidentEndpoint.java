package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Incident;
import br.org.catolicasc.surca.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1")
public class IncidentEndpoint {

    private IncidentRepository incidentDao;

    @Autowired
    public IncidentEndpoint(IncidentRepository incidentDao) {
        this.incidentDao = incidentDao;
    }

    @PostMapping(path = "/admin/incidentes")
    public ResponseEntity<?> save(@RequestBody Incident incident){
        return new ResponseEntity<>(incidentDao.save(incident), HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/incidentes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        incidentDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/incidentes")
    public ResponseEntity<?> deleteAll(@RequestBody List<Incident> incidents){
        for(Incident incident : incidents) {
            incidentDao.delete(incident);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/incidentes")
    public ResponseEntity<?> update(@RequestBody Incident incident){
        return new ResponseEntity<>(incidentDao.save(incident), HttpStatus.OK);
    }
}
