package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.LevelsOfAccess;
import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

public interface UserRepository extends AuditableRepository<User, Long> {
    Page<User> findByName(Pageable pageable, String name);
    Page<User> findByNameStartingWith(Pageable pageable, String name);
    Page<User> findByEmail(Pageable pageable, String name);
    User findByEmail(String name);
    Page<User> findByLevelsOfAccess(@NotNull LevelsOfAccess levelsOfAccess, Pageable pageable);
}
