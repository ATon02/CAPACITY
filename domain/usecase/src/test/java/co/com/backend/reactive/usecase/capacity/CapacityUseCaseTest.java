package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.model.capacity.gateways.CapacityRepository;
import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import co.com.backend.reactive.model.capacitytechnology.gateways.CapacityTechnologyRepository;
import co.com.backend.reactive.model.tecnologydata.TecnologyData;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.usecase.capacity.dto.CapacityCompletedResponse;
import co.com.backend.reactive.usecase.capacity.enums.CapacityError;
import co.com.backend.reactive.usecase.capacity.exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    CapacityRepository capacityRepository;

    @Mock
    CapacityTechnologyRepository capacityTechnologyRepository;

    @Mock
    TecnologyDataRepository tecnologyDataRepository;

    @InjectMocks
    CapacityUseCase useCase;

    @Test
    void save_ok_whenValidCapacityWithValidTechnologies() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        Capacity savedCapacity = Capacity.builder()
                .id(1L)
                .name("Backend Development")
                .description("Backend development capacity")
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(2L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(3L)).thenReturn(Mono.just(true));
        when(capacityRepository.save(any(Capacity.class))).thenReturn(Mono.just(savedCapacity));
        when(capacityTechnologyRepository.save(any(CapacityTechnology.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.save(capacity))
                .assertNext(saved -> {
                    assert saved.getId().equals(1L);
                    assert saved.getName().equals("Backend Development");
                    assert saved.getDescription().equals("Backend development capacity");
                    assert saved.getTechnologies().equals(Set.of(1L, 2L, 3L));
                })
                .verifyComplete();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(tecnologyDataRepository).existsById(3L);
        verify(capacityRepository).save(any(Capacity.class));
        verify(capacityTechnologyRepository, times(3)).save(any(CapacityTechnology.class));
    }

    @Test
    void save_ok_whenExactlyMinimumTechnologies() {
        Capacity capacity = Capacity.builder()
                .name("Frontend Development")
                .description("Frontend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        Capacity savedCapacity = Capacity.builder()
                .id(2L)
                .name("Frontend Development")
                .description("Frontend development capacity")
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(2L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(3L)).thenReturn(Mono.just(true));
        when(capacityRepository.save(any(Capacity.class))).thenReturn(Mono.just(savedCapacity));
        when(capacityTechnologyRepository.save(any(CapacityTechnology.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.save(capacity))
                .assertNext(saved -> {
                    assert saved.getId().equals(2L);
                    assert saved.getName().equals("Frontend Development");
                    assert saved.getDescription().equals("Frontend development capacity");
                    assert saved.getTechnologies().equals(Set.of(1L, 2L, 3L));
                })
                .verifyComplete();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(tecnologyDataRepository).existsById(3L);
        verify(capacityRepository).save(any(Capacity.class));
        verify(capacityTechnologyRepository, times(3)).save(any(CapacityTechnology.class));
    }

    @Test
    void save_ok_whenExactlyMaximumTechnologies() {
        Set<Long> maxTechnologies = Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L);

        Capacity capacity = Capacity.builder()
                .name("Full Stack Development")
                .description("Full stack development capacity")
                .technologies(maxTechnologies)
                .build();

        Capacity savedCapacity = Capacity.builder()
                .id(3L)
                .name("Full Stack Development")
                .description("Full stack development capacity")
                .build();

        for (Long techId : maxTechnologies) {
            when(tecnologyDataRepository.existsById(techId)).thenReturn(Mono.just(true));
        }
        when(capacityRepository.save(any(Capacity.class))).thenReturn(Mono.just(savedCapacity));
        when(capacityTechnologyRepository.save(any(CapacityTechnology.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.save(capacity))
                .assertNext(saved -> {
                    assert saved.getId().equals(3L);
                    assert saved.getName().equals("Full Stack Development");
                    assert saved.getDescription().equals("Full stack development capacity");
                    assert saved.getTechnologies().equals(maxTechnologies);
                })
                .verifyComplete();

        for (Long techId : maxTechnologies) {
            verify(tecnologyDataRepository).existsById(techId);
        }
        verify(capacityRepository).save(any(Capacity.class));
        verify(capacityTechnologyRepository, times(20)).save(any(CapacityTechnology.class));
    }

    @Test
    void getAllCapacitiesWithTechnologies_ok_whenCapacitiesExist() {
        Capacity capacity1 = Capacity.builder()
                .id(1L)
                .name("Backend Development")
                .description("Backend development capacity")
                .build();

        Capacity capacity2 = Capacity.builder()
                .id(2L)
                .name("Frontend Development")
                .description("Frontend development capacity")
                .build();

        TecnologyData tech1 = TecnologyData.builder()
                .id(1L)
                .name("Java")
                .build();

        TecnologyData tech2 = TecnologyData.builder()
                .id(2L)
                .name("React")
                .build();

        when(capacityRepository.findAllPaginated(0, 10, "name", "asc"))
                .thenReturn(Flux.just(capacity1, capacity2));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(1L))
                .thenReturn(Flux.just(1L));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(2L))
                .thenReturn(Flux.just(2L));
        when(tecnologyDataRepository.findByIds(List.of(1L)))
                .thenReturn(Flux.just(tech1));
        when(tecnologyDataRepository.findByIds(List.of(2L)))
                .thenReturn(Flux.just(tech2));

        StepVerifier.create(useCase.getAllCapacitiesWithTechnologies(0, 10, "name", "asc"))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert response.getName().equals("Backend Development");
                    assert response.getDescription().equals("Backend development capacity");
                    assert response.getTechnologies().size() == 1;
                    assert response.getTechnologies().get(0).getId().equals(1L);
                    assert response.getTechnologies().get(0).getName().equals("Java");
                })
                .assertNext(response -> {
                    assert response.getId().equals(2L);
                    assert response.getName().equals("Frontend Development");
                    assert response.getDescription().equals("Frontend development capacity");
                    assert response.getTechnologies().size() == 1;
                    assert response.getTechnologies().get(0).getId().equals(2L);
                    assert response.getTechnologies().get(0).getName().equals("React");
                })
                .verifyComplete();

        verify(capacityRepository).findAllPaginated(0, 10, "name", "asc");
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(1L);
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(2L);
        verify(tecnologyDataRepository).findByIds(List.of(1L));
        verify(tecnologyDataRepository).findByIds(List.of(2L));
    }

    @Test
    void getAllCapacitiesWithTechnologies_ok_whenCapacityHasNoTechnologies() {
        Capacity capacity = Capacity.builder()
                .id(1L)
                .name("Data Science")
                .description("Data science capacity")
                .build();

        when(capacityRepository.findAllPaginated(0, 10, "name", "asc"))
                .thenReturn(Flux.just(capacity));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(1L))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAllCapacitiesWithTechnologies(0, 10, "name", "asc"))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert response.getName().equals("Data Science");
                    assert response.getDescription().equals("Data science capacity");
                    assert response.getTechnologies().isEmpty();
                })
                .verifyComplete();

        verify(capacityRepository).findAllPaginated(0, 10, "name", "asc");
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(1L);
        verify(tecnologyDataRepository, never()).findByIds(any());
    }

    @Test
    void getAllCapacitiesWithTechnologies_ok_whenNoCapacitiesExist() {
        when(capacityRepository.findAllPaginated(0, 10, "name", "asc"))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.getAllCapacitiesWithTechnologies(0, 10, "name", "asc"))
                .verifyComplete();

        verify(capacityRepository).findAllPaginated(0, 10, "name", "asc");
        verify(capacityTechnologyRepository, never()).findTechnologyIdsByCapacityId(anyLong());
        verify(tecnologyDataRepository, never()).findByIds(any());
    }

    @Test
    void getAllCapacitiesWithTechnologies_ok_withDifferentSortingParameters() {
        Capacity capacity = Capacity.builder()
                .id(1L)
                .name("DevOps")
                .description("DevOps capacity")
                .build();

        TecnologyData tech = TecnologyData.builder()
                .id(1L)
                .name("Docker")
                .build();

        when(capacityRepository.findAllPaginated(1, 5, "tech_count", "desc"))
                .thenReturn(Flux.just(capacity));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(1L))
                .thenReturn(Flux.just(1L));
        when(tecnologyDataRepository.findByIds(List.of(1L)))
                .thenReturn(Flux.just(tech));

        StepVerifier.create(useCase.getAllCapacitiesWithTechnologies(1, 5, "tech_count", "desc"))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert response.getName().equals("DevOps");
                    assert response.getDescription().equals("DevOps capacity");
                    assert response.getTechnologies().size() == 1;
                    assert response.getTechnologies().get(0).getId().equals(1L);
                    assert response.getTechnologies().get(0).getName().equals("Docker");
                })
                .verifyComplete();

        verify(capacityRepository).findAllPaginated(1, 5, "tech_count", "desc");
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(1L);
        verify(tecnologyDataRepository).findByIds(List.of(1L));
    }

    @Test
    void findById_ok_whenValidId() {
        Long capacityId = 1L;
        Capacity capacity = Capacity.builder()
                .id(capacityId)
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        when(capacityRepository.findById(capacityId)).thenReturn(Mono.just(capacity));

        StepVerifier.create(useCase.findById(capacityId))
                .assertNext(result -> {
                    assert result.getId().equals(capacityId);
                    assert result.getName().equals("Backend Development");
                    assert result.getDescription().equals("Backend development capacity");
                    assert result.getTechnologies().equals(Set.of(1L, 2L, 3L));
                })
                .verifyComplete();

        verify(capacityRepository).findById(capacityId);
    }

    @Test
    void findById_error_whenCapacityNotFound() {
        Long capacityId = 999L;

        when(capacityRepository.findById(capacityId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.findById(capacityId))
                .expectErrorMatches(ex -> ex instanceof BusinessException
                        && CapacityError.NOT_FOUND.getMessage().equals(ex.getMessage()))
                .verify();

        verify(capacityRepository).findById(capacityId);
    }

    @Test
    void findByIds_ok_whenValidIds() {
        List<Long> ids = List.of(1L, 2L);
        
        Capacity capacity1 = Capacity.builder()
                .id(1L)
                .name("Backend Development")
                .description("Backend development capacity")
                .build();
        
        Capacity capacity2 = Capacity.builder()
                .id(2L)
                .name("Frontend Development")
                .description("Frontend development capacity")
                .build();

        TecnologyData tech1 = TecnologyData.builder()
                .id(1L)
                .name("Java")
                .build();

        TecnologyData tech2 = TecnologyData.builder()
                .id(2L)
                .name("React")
                .build();

        when(capacityRepository.findByIds(ids)).thenReturn(Flux.just(capacity1, capacity2));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(1L)).thenReturn(Flux.just(1L));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(2L)).thenReturn(Flux.just(2L));
        when(tecnologyDataRepository.findByIds(List.of(1L))).thenReturn(Flux.just(tech1));
        when(tecnologyDataRepository.findByIds(List.of(2L))).thenReturn(Flux.just(tech2));

        StepVerifier.create(useCase.findByIds(ids))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert response.getName().equals("Backend Development");
                    assert response.getDescription().equals("Backend development capacity");
                    assert response.getTechnologies().size() == 1;
                    assert response.getTechnologies().get(0).getId().equals(1L);
                    assert response.getTechnologies().get(0).getName().equals("Java");
                })
                .assertNext(response -> {
                    assert response.getId().equals(2L);
                    assert response.getName().equals("Frontend Development");
                    assert response.getDescription().equals("Frontend development capacity");
                    assert response.getTechnologies().size() == 1;
                    assert response.getTechnologies().get(0).getId().equals(2L);
                    assert response.getTechnologies().get(0).getName().equals("React");
                })
                .verifyComplete();

        verify(capacityRepository).findByIds(ids);
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(1L);
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(2L);
        verify(tecnologyDataRepository).findByIds(List.of(1L));
        verify(tecnologyDataRepository).findByIds(List.of(2L));
    }

    @Test
    void findByIds_ok_whenCapacityHasNoTechnologies() {
        List<Long> ids = List.of(1L);
        
        Capacity capacity = Capacity.builder()
                .id(1L)
                .name("Management")
                .description("Management capacity")
                .build();

        when(capacityRepository.findByIds(ids)).thenReturn(Flux.just(capacity));
        when(capacityTechnologyRepository.findTechnologyIdsByCapacityId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(useCase.findByIds(ids))
                .assertNext(response -> {
                    assert response.getId().equals(1L);
                    assert response.getName().equals("Management");
                    assert response.getDescription().equals("Management capacity");
                    assert response.getTechnologies().isEmpty();
                })
                .verifyComplete();

        verify(capacityRepository).findByIds(ids);
        verify(capacityTechnologyRepository).findTechnologyIdsByCapacityId(1L);
        verify(tecnologyDataRepository, never()).findByIds(any());
    }
    
}