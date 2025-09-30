package co.com.backend.reactive.model.capacitytechnology.gateways;

import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

public interface CapacityTechnologyRepository {
    Mono<CapacityTechnology> save(CapacityTechnology capacityTechnology);
    Flux<Long> findTechnologyIdsByCapacityId(Long capacityId);
    Mono<Long> countCapacitiesByTechnologyId(Long technologyId);
    Mono<Void> deleteByCapacityIds(List<Long> capacityIds);
}
