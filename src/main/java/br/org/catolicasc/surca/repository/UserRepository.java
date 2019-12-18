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
    @Query(value = "SELECT * FROM `user` where `name` like :name% AND `status` = 'VISIBLE' group by `name`",
    nativeQuery = true)
    Page<User> findByNameStartingWithAndStatusGroupByName(String name, Pageable pageable);
    @Query(value = "SELECT * FROM `user` where `email` like :email% AND `status` = :status group by `email`",
            nativeQuery = true)
    Page<User> findByEmailStartingWithAndStatusGroupByEmail(String email, String status, Pageable pageable);
    Page<User> findByNameStartingWithAndStatus(String name, Status status, Pageable pageable);
    Page<User> findByEmailStartingWithAndStatus(String email, Status status, Pageable pageable);
    List<User> findByLevelsOfAccess(LevelsOfAccess levelsOfAccess);
    @Query(value = "delete from `user` where email = ?1 and `password` = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByEmailAndPassword(String email, String password);
    Page<User> findByLevelsOfAccessAndStatus(LevelsOfAccess levelsOfAccess, Status status, Pageable pageable);
}
