package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Vet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface VetRepository extends PagingAndSortingRepository<Vet, Long> {
    Vet findByCrmv(String crmv);
    Vet findByUserCode(Long Id);
}
