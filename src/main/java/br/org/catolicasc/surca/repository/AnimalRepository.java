package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Animal;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AnimalRepository extends PagingAndSortingRepository<Animal, Long> {
}
