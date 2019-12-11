package br.org.catolicasc.surca.repository;

import br.org.catolicasc.surca.model.Neighborhood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface NeighborhoodRepository extends PagingAndSortingRepository<Neighborhood, Long> {
    Neighborhood findByNameAndCity_NameAndCity_State_Name(String neighborhood, String city, String state);
    @Query(value = "SELECT * FROM `neighborhood` where `name` like :name% group by `name`",
            nativeQuery = true)
    Page<Neighborhood> findByNameLike(String name, Pageable page);
}
