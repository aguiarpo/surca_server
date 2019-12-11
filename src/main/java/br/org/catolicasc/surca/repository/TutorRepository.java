package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Tutor;

public interface TutorRepository extends AuditableRepository<Tutor, Long> {
    Tutor findByCpf(String cpf);
    Tutor findByRg(String rg);
}
