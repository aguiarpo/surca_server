package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Incident;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IncidentRepository extends PagingAndSortingRepository<Incident, Long> {
}
