package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Vet;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface VetRepository extends PagingAndSortingRepository<Vet, Long> {
    @Transactional
    void removeByUserUserLevelId(Long id);
}
