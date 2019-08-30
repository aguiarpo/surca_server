package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.UserLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserLevelRepository extends PagingAndSortingRepository<UserLevel, Long> {
    Page<UserLevel> findByName(Pageable pageable, String name);
    UserLevel findByName(String name);
}
