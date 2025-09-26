package co.com.backend.reactive.usecase.capacity;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.model.capacity.gateways.CapacityRepository;
import co.com.backend.reactive.model.capacitytechnology.CapacityTechnology;
import co.com.backend.reactive.model.capacitytechnology.gateways.CapacityTechnologyRepository;
import co.com.backend.reactive.model.tecnologydata.gateways.TecnologyDataRepository;
import co.com.backend.reactive.usecase.capacity.enums.CapacityError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    void save_error_whenCapacityNameIsNull() {
        Capacity capacity = Capacity.builder()
                .name(null)
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.CAPACITY_NAME_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenCapacityNameIsBlank() {
        Capacity capacity = Capacity.builder()
                .name("   ")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.CAPACITY_NAME_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenCapacityDescriptionIsNull() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description(null)
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.CAPACITY_DESCRIPTION_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenCapacityDescriptionIsBlank() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("   ")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.CAPACITY_DESCRIPTION_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologiesIsNull() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(null)
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.TECHNOLOGIES_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologiesIsEmpty() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of())
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.TECHNOLOGIES_REQUIRED.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologiesLessThanMinimum() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L))
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.INVALID_TECHNOLOGY_COUNT.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologiesExceedMaximum() {
        Set<Long> tooManyTechnologies = Set.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L);

        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(tooManyTechnologies)
                .build();

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && CapacityError.INVALID_TECHNOLOGY_COUNT.getMessage().equals(ex.getMessage()))
                .verify();

        verify(tecnologyDataRepository, never()).existsById(anyLong());
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologyNotFound() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 999L))
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(2L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(999L)).thenReturn(Mono.just(false));

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && ex.getMessage().contains(CapacityError.TECHNOLOGY_NOT_FOUND.getMessage()))
                .verify();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(tecnologyDataRepository).existsById(999L);
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenTechnologyServiceFails() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(2L)).thenReturn(Mono.error(new RuntimeException("Service unavailable")));

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Service unavailable"))
                .verify();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenCapacityRepositorySaveFails() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 2L, 3L))
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(2L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(3L)).thenReturn(Mono.just(true));
        when(capacityRepository.save(any(Capacity.class)))
                .thenReturn(Mono.error(new RuntimeException("Database connection failed")));

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Database connection failed"))
                .verify();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(tecnologyDataRepository).existsById(3L);
        verify(capacityRepository).save(any(Capacity.class));
        verify(capacityTechnologyRepository, never()).save(any());
    }

    @Test
    void save_error_whenCapacityTechnologyRepositorySaveFails() {
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
                .thenReturn(Mono.error(new RuntimeException("Relationship save failed")));

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof RuntimeException && ex.getMessage().equals("Relationship save failed"))
                .verify();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(2L);
        verify(tecnologyDataRepository).existsById(3L);
        verify(capacityRepository).save(any(Capacity.class));
        verify(capacityTechnologyRepository).save(any(CapacityTechnology.class));
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
    void save_error_whenMultipleTechnologiesNotFound() {
        Capacity capacity = Capacity.builder()
                .name("Backend Development")
                .description("Backend development capacity")
                .technologies(Set.of(1L, 998L, 999L))
                .build();

        when(tecnologyDataRepository.existsById(1L)).thenReturn(Mono.just(true));
        when(tecnologyDataRepository.existsById(998L)).thenReturn(Mono.just(false));
        when(tecnologyDataRepository.existsById(999L)).thenReturn(Mono.just(false));

        StepVerifier.create(useCase.save(capacity))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException
                        && ex.getMessage().contains(CapacityError.TECHNOLOGY_NOT_FOUND.getMessage()))
                .verify();

        verify(tecnologyDataRepository).existsById(1L);
        verify(tecnologyDataRepository).existsById(998L);
        verify(capacityRepository, never()).save(any());
        verify(capacityTechnologyRepository, never()).save(any());
    }
}