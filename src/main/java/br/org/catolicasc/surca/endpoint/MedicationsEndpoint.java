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
    public ResponseEntity<?> save(@RequestBody List<Medications> medications){
        updateOrSave(medications);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/veterinario/medicacoes/remover")
    public ResponseEntity<?> deleteAll(@RequestBody List<Medications> medications){
        for (Medications medication: medications) {
            animalMedicationsDao.deleteByMedicationCode(medication.getCode());
        }
        medicationsDao.deleteAll(medications);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/veterinario/medicacoes")
    public ResponseEntity<?> update(@RequestBody List<Medications> medications){
        updateOrSave(medications);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateOrSave(List<Medications> medications){
        for (Medications medication: medications) {
            Medications find = medicationsDao.findByName(medication.getName());
            if(find == null)medicationsDao.save(medication);
        }
    }
}
