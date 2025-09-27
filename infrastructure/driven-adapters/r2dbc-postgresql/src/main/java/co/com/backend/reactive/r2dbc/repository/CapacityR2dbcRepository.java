package co.com.backend.reactive.r2dbc.repository;

import co.com.backend.reactive.r2dbc.entity.CapacityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CapacityR2dbcRepository extends ReactiveCrudRepository<CapacityEntity, Long>, ReactiveQueryByExampleExecutor<CapacityEntity> {
    Flux<CapacityEntity> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT c.id, c.name, c.description, " +
       "COUNT(ct.technology_id) as tech_count " +
       "FROM capacities c " +
       "LEFT JOIN capacity_technology ct ON c.id = ct.capacity_id " +
       "GROUP BY c.id, c.name, c.description " +
       "ORDER BY " +
       "CASE WHEN :sortBy = 'name' AND :sortDirection = 'asc' THEN c.name ELSE NULL END ASC, " +
       "CASE WHEN :sortBy = 'name' AND :sortDirection = 'desc' THEN c.name ELSE NULL END DESC, " +
       "CASE WHEN :sortBy = 'tech_count' AND :sortDirection = 'asc' THEN COUNT(ct.technology_id) ELSE NULL END ASC, " +
       "CASE WHEN :sortBy = 'tech_count' AND :sortDirection = 'desc' THEN COUNT(ct.technology_id) ELSE NULL END DESC " +
       "LIMIT :size OFFSET :offset")
    Flux<CapacityEntity> findAllPaginated(@Param("offset") int offset,
                                        @Param("size") int size,
                                        @Param("sortBy") String sortBy,
                                        @Param("sortDirection") String sortDirection);

}