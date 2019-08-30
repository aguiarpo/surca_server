package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.User;
import br.org.catolicasc.surca.model.UserLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Transactional
    void removeByUserLevelId(Long id);
    Page<User> findByName(Pageable pageable, String name);
    Page<User> findByNameStartingWith(Pageable pageable, String name);
    Page<User> findByEmail(Pageable pageable, String name);
    Page<User> findByUserLevelName(Pageable pageable, String name);
}
