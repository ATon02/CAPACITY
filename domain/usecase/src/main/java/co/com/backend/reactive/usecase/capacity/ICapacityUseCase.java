package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import reactor.core.publisher.Mono;

public interface ICapacityUseCase {
    Mono<Capacity> save(Capacity capacity);
}
