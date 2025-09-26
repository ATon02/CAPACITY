package co.com.backend.reactive.r2dbc.repository;

import co.com.backend.reactive.r2dbc.entity.CapacityEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CapacityR2dbcRepository extends ReactiveCrudRepository<CapacityEntity, Long>, ReactiveQueryByExampleExecutor<CapacityEntity> {
    Flux<CapacityEntity> findByNameContainingIgnoreCase(String name);
}