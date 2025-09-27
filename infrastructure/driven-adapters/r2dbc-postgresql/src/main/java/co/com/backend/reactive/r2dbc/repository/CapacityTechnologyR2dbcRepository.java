package co.com.backend.reactive.r2dbc.repository;

import co.com.backend.reactive.r2dbc.entity.CapacityTechnologyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CapacityTechnologyR2dbcRepository extends ReactiveCrudRepository<CapacityTechnologyEntity, Long>, ReactiveQueryByExampleExecutor<CapacityTechnologyEntity> {
    Flux<CapacityTechnologyEntity> findByCapacityId(Long capacityId);
    Mono<Void> deleteByCapacityId(Long capacityId);
    
    @Query("SELECT technology_id FROM capacity_technology WHERE capacity_id = :capacityId")
    Flux<Long> findTechnologyIdsByCapacityId(Long capacityId);
}