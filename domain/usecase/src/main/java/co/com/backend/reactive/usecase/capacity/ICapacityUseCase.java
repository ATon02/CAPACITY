package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.usecase.capacity.dto.CapacityResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICapacityUseCase {
    Mono<Capacity> save(Capacity capacity);
    Flux<CapacityResponseDTO> getAllCapacitiesWithTechnologies(int page, int size, String sortBy, String sortDirection);
}
