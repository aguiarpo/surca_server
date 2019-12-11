package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Animal;
import br.org.catolicasc.surca.model.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends AuditableRepository<Animal, Long> {
    List<Animal> findByTutorCode(Long idTutor);
    Animal findByMicrochipNumber(String microchipNumber);
    @Query(value = "SELECT count(id) FROM animal WHERE tutor_id = :tutor_id and status = :status", nativeQuery = true)
    int findByTutorWithAnimal(@Param("tutor_id") Long tutorId, @Param("status") Status status);

    @Query(value = "SELECT tutor_id FROM animal WHERE id = :id", nativeQuery = true)
    Long findIdByIdTutor(@Param("id") Long id);

    @Query(value = "SELECT if(animal.genre = 'Fêmea', count(animal.id), 0) as female, " +
            "if(animal.genre = 'Macho', count(animal.id), 0) as male, " +
            "if(animal.castrated = 1, count(animal.id), 0) as castrated," +
            "if(animal.castrated = 0, count(animal.id), 0) as notCastrated," +
            "if(animal.species = 'Gato', count(animal.id), 0) as cat," +
            "if(animal.species = 'Cachorro', count(animal.id), 0) as dog," +
            "if(animal.species = 'Outro', count(animal.id), 0) as other," +
            "state.name as state, city.name as city, neighborhood.name as neighborhood FROM animal " +
            "JOIN tutor ON tutor.id = animal.tutor_id " +
            "JOIN neighborhood ON neighborhood.id = tutor.neighborhood_id " +
            "JOIN city ON city.id = neighborhood.city_id " +
            "JOIN state ON state.id = city.state_id " +
            "WHERE neighborhood.name = :neighborhood " +
            "GROUP BY state.name, city.name, neighborhood.name", nativeQuery = true)
    List<Report> findByNeighborhood(String neighborhood);

    @Query(value = "SELECT if(animal.genre = 'Fêmea', count(animal.id), 0) as female, " +
            "if(animal.genre = 'Macho', count(animal.id), 0) as male, " +
            "if(animal.castrated = 1, count(animal.id), 0) as castrated," +
            "if(animal.castrated = 0, count(animal.id), 0) as notCastrated," +
            "if(animal.species = 'Gato', count(animal.id), 0) as cat," +
            "if(animal.species = 'Cachorro', count(animal.id), 0) as dog," +
            "if(animal.species = 'Outro', count(animal.id), 0) as other," +
            "state.name as state, city.name as city, neighborhood.name as neighborhood FROM animal " +
            "JOIN tutor ON tutor.id = animal.tutor_id " +
            "JOIN neighborhood ON neighborhood.id = tutor.neighborhood_id " +
            "JOIN city ON city.id = neighborhood.city_id " +
            "JOIN state ON state.id = city.state_id " +
            "GROUP BY state.name, city.name, neighborhood.name", nativeQuery = true)
    List<Report> findByAllNeighborhood();
}