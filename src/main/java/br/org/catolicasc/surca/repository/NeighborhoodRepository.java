package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Neighborhood;
import org.springframework.data.repository.CrudRepository;

public interface NeighborhoodRepository extends CrudRepository<Neighborhood, Long> {
    Neighborhood findByNameAndCity_NameAndCity_State_Name(String neighborhood, String city, String state);
}
