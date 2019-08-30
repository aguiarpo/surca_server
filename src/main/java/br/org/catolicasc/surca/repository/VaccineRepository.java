package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Vaccine;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VaccineRepository extends PagingAndSortingRepository<Vaccine, Long> {
}
