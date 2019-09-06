package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Vaccine;
import br.org.catolicasc.surca.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
public class VaccineEndpoint {

    private VaccineRepository vaccineDao;

    @Autowired
    public VaccineEndpoint(VaccineRepository vaccineDao) {
        this.vaccineDao = vaccineDao;
    }

    @GetMapping(path = "/veterinario/vacinas")
    public ResponseEntity<?> listAll(Pageable pageable){
        return new ResponseEntity<>(vaccineDao.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/vacinas/{id}")
    public ResponseEntity<?> getVaccineById(@PathVariable("id")Long id){
        return new ResponseEntity<>(vaccineDao.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/vacinas/nome/{name}")
    public ResponseEntity<?> getNivelUsuarioById(@PathVariable("name") String name, Pageable pageable){
        Page<Vaccine> vaccines = vaccineDao.findByName(pageable, name);
        return new ResponseEntity<>(vaccines, HttpStatus.OK);
    }

    @GetMapping(path = "/veterinario/vacinas/nome/like/{name}")
    public ResponseEntity<?> getVaccineByNameLike(@PathVariable("name") String name, Pageable pageable){
        Page<Vaccine> vaccines = vaccineDao.findByNameStartingWith(pageable, name);
        return new ResponseEntity<>(vaccines, HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/vacinas")
    public ResponseEntity<?> save(@RequestBody Vaccine vaccine){
        return new ResponseEntity<>(vaccineDao.save(vaccine), HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/vacinas/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        vaccineDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(path = "/veterinario/vacinas")
    public ResponseEntity<?> update(@RequestBody Vaccine vaccine){
        return new ResponseEntity<>(vaccineDao.save(vaccine), HttpStatus.OK);
    }
}
