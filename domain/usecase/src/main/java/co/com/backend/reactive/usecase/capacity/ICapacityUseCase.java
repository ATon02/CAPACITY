package co.com.backend.reactive.usecase.capacity;

import java.util.List;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.usecase.capacity.dto.CapacityCompletedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityUseCase {
    Mono<Capacity> save(Capacity capacity);
    Flux<CapacityCompletedResponse> getAllCapacitiesWithTechnologies(int page, int size, String sortBy, String sortDirection);
    Mono<Capacity> findById(Long id);
    Flux<CapacityCompletedResponse> findByIds(List<Long> ids);
}
