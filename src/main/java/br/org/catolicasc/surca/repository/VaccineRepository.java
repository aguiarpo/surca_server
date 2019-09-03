package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Vaccine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VaccineRepository extends AuditableRepository<Vaccine, Long> {
    Page<Vaccine> findByName(Pageable pageable, String name);
}
