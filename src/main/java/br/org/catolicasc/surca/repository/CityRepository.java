package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {
    City findByNameAndStateName(String city, String state);
}
