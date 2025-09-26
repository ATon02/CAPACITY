package co.com.backend.reactive.model.capacitytechnology.gateways;

import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import reactor.core.publisher.Mono;

public interface CapacityTechnologyRepository {
    Mono<CapacityTechnology> save(CapacityTechnology capacityTechnology);
}
