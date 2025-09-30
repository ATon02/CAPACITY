package co.com.backend.reactive.r2dbc.adapter;

import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import co.com.backend.reactive.model.capacitytechnology.gateways.CapacityTechnologyRepository;
import co.com.backend.reactive.r2dbc.entity.CapacityTechnologyEntity;
import co.com.backend.reactive.r2dbc.helper.ReactiveAdapterOperations;
import co.com.backend.reactive.r2dbc.repository.CapacityTechnologyR2dbcRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Repository
public class CapacityTechnologyEntityR2dbcRepositoryAdapter extends ReactiveAdapterOperations<
        CapacityTechnology,
        CapacityTechnologyEntity,
        Long,
        CapacityTechnologyR2dbcRepository
        > implements CapacityTechnologyRepository {

    public CapacityTechnologyEntityR2dbcRepositoryAdapter(CapacityTechnologyR2dbcRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, CapacityTechnology.class));
    }

    @Override
    public Mono<CapacityTechnology> save(CapacityTechnology capacityTechnology) {
        return super.save(capacityTechnology);
    }

    @Override
    public Flux<Long> findTechnologyIdsByCapacityId(Long capacityId) {
        return repository.findTechnologyIdsByCapacityId(capacityId);
    }

    @Override
    public Mono<Long> countCapacitiesByTechnologyId(Long technologyId) {
        return repository.countCapacitiesByTechnologyId(technologyId);
    }

    @Override
    public Mono<Void> deleteByCapacityIds(List<Long> capacityIds) {
        return repository.deleteByCapacityIds(capacityIds);
    }
}
