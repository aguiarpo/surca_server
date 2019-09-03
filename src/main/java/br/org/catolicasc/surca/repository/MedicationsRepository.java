package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Medications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicationsRepository extends AuditableRepository<Medications, Long> {
    Page<Medications> findByName(Pageable pageable, String name);
}
