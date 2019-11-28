package br.org.catolicasc.surca.repository;
import br.org.catolicasc.surca.model.AnimalMedications;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface AnimalMedicationsRepository extends CrudRepository<AnimalMedications, Long>{
    @Transactional
    void deleteByMedicationCode(Long code);
    @Transactional
    void deleteByAnimalCode(Long code);
}
