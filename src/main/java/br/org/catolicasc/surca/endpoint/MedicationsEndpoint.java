package br.org.catolicasc.surca.endpoint;

import br.org.catolicasc.surca.model.Medications;
import br.org.catolicasc.surca.repository.AnimalMedicationsRepository;
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
    private AnimalMedicationsRepository animalMedicationsDao;

    @Autowired
    public MedicationsEndpoint(MedicationsRepository medicationsDao, AnimalMedicationsRepository animalMedicationsDao) {
        this.medicationsDao = medicationsDao;
        this.animalMedicationsDao = animalMedicationsDao;
    }

    @PostMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> save(@RequestBody Medications medication){
        updateOrSave(medication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "/veterinario/medicacoes/{id}")
    public ResponseEntity<?> deleteAll(@PathVariable Long id){
        animalMedicationsDao.deleteByMedicationCode(id);
        medicationsDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> update(@RequestBody Medications medication){
        updateOrSave(medication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateOrSave(Medications medication){
            Medications find = medicationsDao.findByName(medication.getName());
            if(find == null)medicationsDao.save(medication);
    }
}
