package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.model.capacity.gateways.CapacityRepository;
import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import co.com.backend.reactive.model.capacitytechnology.gateways.CapacityTechnologyRepository;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.usecase.capacity.utils.CapacityValidator;
import co.com.backend.reactive.usecase.capacity.enums.CapacityError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.Set;

@RequiredArgsConstructor
public class CapacityUseCase implements ICapacityUseCase {
    
    private final CapacityRepository capacityRepository;
    private final CapacityTechnologyRepository capacityTechnologyRepository;
    private final TecnologyDataRepository tecnologyDataRepository;

    public Mono<Capacity> save(Capacity capacity) {
        return CapacityValidator.validateForSave(capacity)
                .flatMap(validatedCapacity -> validateAndCreateTechnologies(validatedCapacity))
                .flatMap(this::saveCapacityWithTechnologies);
    }
    
    private Mono<Capacity> validateAndCreateTechnologies(Capacity capacity) {
        if (capacity.getTechnologies() == null || capacity.getTechnologies().isEmpty()) {
            return Mono.error(new IllegalArgumentException(CapacityError.TECHNOLOGIES_REQUIRED.getMessage()));
        }
        return Flux.fromIterable(capacity.getTechnologies())
                .flatMap(this::validateTechnologyExists)
                .collectList()
                .map(validatedTechIds -> capacity);
    }
    
    private Mono<Long> validateTechnologyExists(Long technologyId) {
        return tecnologyDataRepository.existsById(technologyId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException(CapacityError.TECHNOLOGY_NOT_FOUND.getMessage()+" " + technologyId));
                    }
                    return Mono.just(technologyId);
                });
    }
    
    private Mono<Capacity> saveCapacityWithTechnologies(Capacity capacity) {
        return capacityRepository.save(capacity)
                .flatMap(savedCapacity -> {
                    Capacity capacityWithTechnologies = Capacity.builder()
                            .id(savedCapacity.getId())
                            .name(savedCapacity.getName())
                            .description(savedCapacity.getDescription())
                            .technologies(capacity.getTechnologies())
                            .build();
                    
                    return saveCapacityTechnologies(capacityWithTechnologies)
                            .thenReturn(capacityWithTechnologies);
                });
    }
    
    private Mono<Void> saveCapacityTechnologies(Capacity capacity) {
        return Flux.fromIterable(capacity.getTechnologies())
                .map(technologyId -> {
                    CapacityTechnology capTech = CapacityTechnology.builder()
                            .capacityId(capacity.getId())
                            .technologyId(technologyId)
                            .build();
                    return capTech;
                })
                .flatMap(capacityTechnology -> {
                    return capacityTechnologyRepository.save(capacityTechnology);
                })
                .then();
    }
}
