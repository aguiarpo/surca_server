package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.LevelsOfAccess;
import br.org.catolicasc.surca.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends AuditableRepository<User, Long> {
    Page<User> findByName(Pageable pageable, String name);
    Page<User> findByNameStartingWith(Pageable pageable, String name);
    Page<User> findByEmail(Pageable pageable, String email);
    Page<User> findByCityStartingWith(Pageable pageable, String city);
    Page<User> findByCity(Pageable pageable, String city);
    Page<User> findByState(Pageable pageable, String state);
    User findByEmail(String email);
    @Query(value = "delete from `user` where email = ?1 and `password` = ?2", nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByEmailAndPassword(String email, String password);
    Page<User> findByLevelsOfAccess(LevelsOfAccess levelsOfAccess, Pageable pageable);
    @Query(value = "select * from `user` where email = ?1", nativeQuery = true)
    User findByEmailWithReturnPassword(String email);
}
