package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Status;
import br.org.catolicasc.surca.model.Tutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TutorRepository extends AuditableRepository<Tutor, Long> {
    Page<Tutor> findByNameAndStatus(Pageable pageable, String name, Status status);
    Page<Tutor> findByNameStartingWithAndStatus(Pageable pageable, String name, Status status);
    Tutor findByCpfAndStatus(String cpf, Status status);
    Tutor findByCpf(String cpf);
    Page<Tutor> findByRgAndStatus(Pageable pageable, String rg, Status status);
}
