package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.LevelsOfAccess;
import br.org.catolicasc.surca.model.Status;
import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends AuditableRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndStatus(String email, Status status);
    List<User> findByLevelsOfAccessNot(LevelsOfAccess levelsOfAccess);
    @Query(value = "delete from `user` where email = ?1 and `password` = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByEmailAndPassword(String email, String password);
    Page<User> findByLevelsOfAccessAndStatus(LevelsOfAccess levelsOfAccess, Status status, Pageable pageable);
}
