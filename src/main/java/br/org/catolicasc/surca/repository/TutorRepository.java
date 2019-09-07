package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TutorRepository extends AuditableRepository<Tutor, Long> {
    Page<Tutor> findByName(Pageable pageable, String name);
    Page<Tutor> findByNameStartingWith(Pageable pageable, String name);
    Page<Tutor> findByCpf(Pageable pageable, String cpf);
    Tutor findByCpf(String cpf);
    Page<Tutor> findByRg(Pageable pageable, String rg);
    Page<Tutor> findByCityStartingWith(Pageable pageable, String city);
    Page<Tutor> findByState(Pageable pageable, String state);
}
