package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Medications;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MedicationsRepository extends PagingAndSortingRepository<Medications, Long> {
}
