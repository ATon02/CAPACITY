package co.com.backend.reactive.r2dbc.adapter;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.model.capacity.gateways.CapacityRepository;
import co.com.backend.reactive.r2dbc.entity.CapacityEntity;
import co.com.backend.reactive.r2dbc.helper.ReactiveAdapterOperations;
import co.com.backend.reactive.r2dbc.repository.CapacityR2dbcRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CapacityR2dbcRepositoryAdapter extends ReactiveAdapterOperations<
        Capacity,
        CapacityEntity,
        Long,
        CapacityR2dbcRepository
        > implements CapacityRepository {

    public CapacityR2dbcRepositoryAdapter(CapacityR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Capacity.class));
    }

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        return super.save(capacity);
    }
}