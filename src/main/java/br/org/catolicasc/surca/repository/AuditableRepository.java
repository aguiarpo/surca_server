package br.org.catolicasc.surca.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AuditableRepository <T, ID> extends PagingAndSortingRepository<T, ID>{
    Page<T> findByNameStartingWith(Pageable pageable, String name);
}
