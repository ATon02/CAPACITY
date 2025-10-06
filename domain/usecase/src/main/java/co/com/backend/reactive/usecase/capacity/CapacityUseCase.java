package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.model.capacity.gateways.CapacityRepository;
import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import co.com.backend.reactive.model.capacitytechnology.gateways.CapacityTechnologyRepository;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.usecase.capacity.dto.CapacityCompletedResponse;
import co.com.backend.reactive.usecase.capacity.dto.TechnologyDTO;
import co.com.backend.reactive.usecase.capacity.enums.CapacityError;
import co.com.backend.reactive.usecase.capacity.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
public class CapacityUseCase implements ICapacityUseCase {

    private final CapacityRepository capacityRepository;
    private final CapacityTechnologyRepository capacityTechnologyRepository;
    private final TecnologyDataRepository tecnologyDataRepository;

    @Override
    public Mono<Capacity> save(Capacity capacity) {
        return validateAndCreateTechnologies(capacity)
                .flatMap(this::saveCapacityWithTechnologies);
    }

    private Mono<Capacity> validateAndCreateTechnologies(Capacity capacity) {
        if (capacity.getTechnologies() == null || capacity.getTechnologies().isEmpty()) {
            return Mono.error(new BusinessException(CapacityError.TECHNOLOGIES_REQUIRED.getMessage()));
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
                        return Mono.error(new BusinessException(
                                CapacityError.TECHNOLOGY_NOT_FOUND.getMessage() + " " + technologyId));
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
                    return CapacityTechnology.builder()
                            .capacityId(capacity.getId())
                            .technologyId(technologyId)
                            .build();
                })
                .flatMap(capacityTechnologyRepository::save)
                .then();
    }

    @Override
    public Flux<CapacityCompletedResponse> getAllCapacitiesWithTechnologies(int page, int size, String sortBy,
            String sortDirection) {
        return capacityRepository.findAllPaginated(page, size, sortBy, sortDirection)
                .concatMap(capacity -> capacityTechnologyRepository.findTechnologyIdsByCapacityId(capacity.getId())
                        .collectList()
                        .flatMap(technologyIds -> {
                            if (technologyIds.isEmpty()) {
                                return Mono.just(CapacityCompletedResponse.builder()
                                        .id(capacity.getId())
                                        .name(capacity.getName())
                                        .description(capacity.getDescription())
                                        .technologies(List.of())
                                        .build());
                            }

                            return tecnologyDataRepository.findByIds(technologyIds)
                                    .onErrorResume(error -> Flux.empty())
                                    .map(techData -> TechnologyDTO.builder()
                                            .id(techData.getId())
                                            .name(techData.getName())
                                            .build())
                                    .collectList()
                                    .map(technologies -> CapacityCompletedResponse.builder()
                                            .id(capacity.getId())
                                            .name(capacity.getName())
                                            .description(capacity.getDescription())
                                            .technologies(technologies)
                                            .build());
                        }));
    }

    @Override
    public Mono<Capacity> findById(Long id) {
        return capacityRepository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CapacityError.NOT_FOUND.getMessage())));
    }

    @Override
    public Flux<CapacityCompletedResponse> findByIds(List<Long> ids) {
        return capacityRepository.findByIds(ids)
                .concatMap(capacity -> capacityTechnologyRepository.findTechnologyIdsByCapacityId(capacity.getId())
                        .collectList()
                        .flatMap(technologyIds -> {
                            if (technologyIds.isEmpty()) {
                                return Mono.just(CapacityCompletedResponse.builder()
                                        .id(capacity.getId())
                                        .name(capacity.getName())
                                        .description(capacity.getDescription())
                                        .technologies(List.of())
                                        .build());
                            }

                            return tecnologyDataRepository.findByIds(technologyIds)
                                    .onErrorResume(error -> Flux.empty())
                                    .map(techData -> TechnologyDTO.builder()
                                            .id(techData.getId())
                                            .name(techData.getName())
                                            .build())
                                    .collectList()
                                    .map(technologies -> CapacityCompletedResponse.builder()
                                            .id(capacity.getId())
                                            .name(capacity.getName())
                                            .description(capacity.getDescription())
                                            .technologies(technologies)
                                            .build());
                        }));
    }

    @Override
    public Mono<Void> deleteCapacities(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(capacityTechnologyRepository::findTechnologyIdsByCapacityId)
                .distinct()
                .collectList()
                .flatMap(this::findSingleTecnologies)
                .flatMap(singleTechnologyIds -> {
                    return capacityTechnologyRepository.deleteByCapacityIds(ids)
                            .then(Mono.defer(() -> {
                                if (!singleTechnologyIds.isEmpty()) {
                                    return tecnologyDataRepository.deleteByIds(singleTechnologyIds)
                                            .then(capacityRepository.deleteCapacitiesByIds(ids));
                                } else {
                                    return capacityRepository.deleteCapacitiesByIds(ids);
                                }
                            }));
                });
    }

    private Mono<List<Long>> findSingleTecnologies(List<Long> technologyIds) {
        return Flux.fromIterable(technologyIds)
                .filterWhen(technologyId -> 
                    capacityTechnologyRepository.countCapacitiesByTechnologyId(technologyId)
                            .map(count -> count == 1)
                )
                .collectList();
    }

}
