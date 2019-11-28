package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Animal;
import br.org.catolicasc.surca.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends AuditableRepository<Animal, Long> {
    Page<Animal> findByNameAndStatus(Pageable pageable, String name, Status status);
    Page<Animal> findBySpeciesAndStatus(Pageable pageable, String species, Status status);
    Page<Animal> findByBreedAndStatus(Pageable pageable, String breed, Status status);
    List<Animal> findByTutorCode(Long idTutor);
    Animal findByMicrochipNumberAndStatus(String microchipNumber, Status status);
    Animal findByMicrochipNumber(String microchipNumber);

    @Query(value = "SELECT count(id) FROM animal WHERE tutor_id = :tutor_id and status = :status", nativeQuery = true)
    int findByTutorWithAnimal(@Param("tutor_id") Long tutorId, @Param("status") Status status);

    @Query(value = "SELECT tutor_id FROM animal WHERE id = :id", nativeQuery = true)
    Long findIdByIdTutor(@Param("id") Long id);

    Page<Animal> findByNameStartingWithAndStatus(Pageable pageable, String name, Status status);
    Page<Animal> findByStatus(Pageable pageable, Status status);

}