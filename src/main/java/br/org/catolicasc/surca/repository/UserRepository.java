package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @Transactional
    void removeByUserLevelId(Long id);
}
