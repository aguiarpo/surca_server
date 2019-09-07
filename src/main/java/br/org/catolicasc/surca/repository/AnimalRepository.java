package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnimalRepository extends AuditableRepository<Animal, Long> {
    Page<Animal> findByName(Pageable pageable, String name);
    Page<Animal> findBySpecies(Pageable pageable, String species);
    Page<Animal> findByBreed(Pageable pageable, String breed);
    List<Animal> findByTutorId(Long idTutor);
    Animal findByMicrochipNumber(String microchipNumber);
    @Query(value = "SELECT * FROM animal where vet_microchip_id = ?1 or castrator_id = ?2", nativeQuery = true)
    List<Animal> findByVetMicrochipIdOrCastratorId(Long idVet, Long idCastrator);

    @Query(value = "SELECT count(id) FROM animal WHERE tutor_id = :tutor_id ", nativeQuery = true)
    int findByTutorWithAnimal(@Param("tutor_id") Long tutorId);

    @Query(value = "SELECT tutor_id FROM animal WHERE id = :id", nativeQuery = true)
    Long findIdByIdTutor(@Param("id") Long id);

    @Query(value = "SELECT * FROM animal WHERE YEAR(date_microchip) like concat('%', :age) " +
            "AND MONTH(date_microchip)=:month AND DAY(date_microchip)=:day" , nativeQuery = true)
    Page<Animal> findByDateMicrochip(Pageable pageable, @Param("age") int age,
                                          @Param("month") int month, @Param("day") int day);

    @Query(value = "SELECT * FROM animal WHERE YEAR(date_microchip) like concat('%', :age)" , nativeQuery = true)
    Page<Animal> findDateMicrochipByAge(Pageable pageable, @Param("age") int age);

    @Query(value = "SELECT * FROM animal WHERE YEAR(date_microchip) like concat('%', :age) AND MONTH(date_microchip)=:month" , nativeQuery = true)
    Page<Animal> findDateMicrochipByMouth(Pageable pageable,@Param("age") int age, @Param("month") int month);

    Page<Animal> findByNameStartingWith(Pageable pageable, String name);

    @Query(value = "SELECT * FROM animal WHERE YEAR(birth_date) like concat('%', :age) " +
            "AND MONTH(birth_date)=:month AND DAY(birth_date)=:day" , nativeQuery = true)
    Page<Animal> findByBirthDate(Pageable pageable, @Param("age") int age,
                                      @Param("month") int month, @Param("day") int day);

    @Query(value = "SELECT * FROM animal WHERE YEAR(birth_date) like concat('%', :age)" , nativeQuery = true)
    Page<Animal> findBirthDateByAge(Pageable pageable, @Param("age") int age);

    @Query(value = "SELECT * FROM animal WHERE YEAR(birth_date) like concat('%', :age) AND MONTH(birth_date)=:month" , nativeQuery = true)
    Page<Animal> findBirthDateByMouth(Pageable pageable, @Param("age") int age, @Param("month") int month);
}