package co.com.backend.reactive.r2dbc.repository;

import co.com.backend.reactive.r2dbc.entity.CapacityTechnologyEntity;
import co.com.backend.reactive.r2dbc.helper.ConstRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public interface CapacityTechnologyR2dbcRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long>, ReactiveQueryByExampleExecutor<CapacityTechnologyEntity> {
    Flux<CapacityTechnologyEntity> findByCapacityId(Long capacityId);
    Mono<Void> deleteByCapacityId(Long capacityId);
    
    @Query(ConstRepository.FIND_TECHNOLOGY_IDS_BY_CAPACITY_ID)
    Flux<Long> findTechnologyIdsByCapacityId(Long capacityId);
    
    @Query(ConstRepository.COUNT_CAPACITIES_BY_TECHNOLOGY_ID)
    Mono<Long> countCapacitiesByTechnologyId(Long technologyId);
    
    @Query(ConstRepository.DELETE_BY_CAPACITY_IDS)
    Mono<Void> deleteByCapacityIds(List<Long> capacityIds);
}