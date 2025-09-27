package co.com.backend.reactive.model.capacity.gateways;

import co.com.backend.reactive.model.capacity.Capacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CapacityRepository {
    Mono<Capacity> save(Capacity capacity);
    Flux<Capacity> findAllPaginated(int page, int size, String sortBy, String sortDirection);
}
