package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.State;
import org.springframework.data.repository.CrudRepository;

public interface StateRepository extends CrudRepository<State, Long> {
    State findByName(String name);
}
