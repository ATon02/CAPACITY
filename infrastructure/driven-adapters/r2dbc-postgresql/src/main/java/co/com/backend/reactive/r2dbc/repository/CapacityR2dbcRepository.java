package co.com.backend.reactive.r2dbc.repository;

import co.com.backend.reactive.r2dbc.entity.CapacityEntity;
import co.com.backend.reactive.r2dbc.helper.ConstRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CapacityR2dbcRepository extends ReactiveCrudRepository<CapacityEntity, Long>, ReactiveQueryByExampleExecutor<CapacityEntity> {
    Flux<CapacityEntity> findByNameContainingIgnoreCase(String name);
    
    @Query(ConstRepository.FIND_ALL_PAGINATED)
    Flux<CapacityEntity> findAllPaginated(@Param("offset") int offset,
                                        @Param("size") int size,
                                        @Param("sortBy") String sortBy,
                                        @Param("sortDirection") String sortDirection);

}