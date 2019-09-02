package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;

public interface UserRepository extends AuditableRepository<User, Long> {
    @Transactional
    void removeByUserLevelId(Long id);
    Page<User> findByName(Pageable pageable, String name);
    Page<User> findByNameStartingWith(Pageable pageable, String name);
    Page<User> findByEmail(Pageable pageable, String name);
    Page<User> findByUserLevelName(Pageable pageable, String name);
}
