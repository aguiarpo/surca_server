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
    public ResponseEntity<?> save(@RequestBody List<Incident> incidents){
        updateOrSave(incidents);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/admin/incidentes/remover")
    public ResponseEntity<?> deleteAll(@RequestBody List<Incident> incidents){
        incidentDao.deleteAll(incidents);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/admin/incidentes")
    public ResponseEntity<?> update(@RequestBody List<Incident> incidents){
        updateOrSave(incidents);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateOrSave(List<Incident> incidents){
        for (Incident incident: incidents) {
            Incident find = incidentDao.findByName(incident.getName());
            if(find == null)incidentDao.save(incident);
            else{
                incident.setCode(find.getCode());
                incidentDao.save(incident);
            }
        }
    }
}
