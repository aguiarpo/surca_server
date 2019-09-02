package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.UserLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserLevelRepository extends AuditableRepository<UserLevel, Long> {
    Page<UserLevel> findByName(Pageable pageable, String name);
    UserLevel findByName(String name);
}
