package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Medications;
import br.org.catolicasc.surca.repository.MedicationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1")
public class MedicationsEndpoint {

    private MedicationsRepository medicationsDao;

    @Autowired
    public MedicationsEndpoint(MedicationsRepository medicationsDao) {
        this.medicationsDao = medicationsDao;
    }

    @GetMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(medicationsDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/medicacoes/{id}")
    public ResponseEntity<?> getVaccineById(@PathVariable("id")Long id){
        return new ResponseEntity<>(medicationsDao.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/medicacoes/nome/{name}")
    public ResponseEntity<?> getNivelUsuarioById(@PathVariable("name") String name, Pageable pageable){
        Page<Medications> medications = medicationsDao.findByName(pageable, name);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/medicacoes/nome/like/{name}")
    public ResponseEntity<?> getMedicationByNameLike(@PathVariable("name") String name, Pageable pageable){
        Page<Medications> medications = medicationsDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> save(@RequestBody Medications medications){
        return new ResponseEntity<>(medicationsDao.save(medications), HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/medicacoes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        medicationsDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> deleteAll(@RequestBody List<Medications> medications){
        for(Medications medication : medications) {
            medicationsDao.delete(medication);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> update(@RequestBody Medications medications){
        return new ResponseEntity<>(medicationsDao.save(medications), HttpStatus.OK);
    }
}
