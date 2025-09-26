package co.com.backend.reactive.usecase.capacity.utils;

import co.com.backend.reactive.model.capacity.Capacity;
import co.com.backend.reactive.usecase.capacity.enums.CapacityError;
import reactor.core.publisher.Mono;
import java.util.HashSet;
import java.util.Set;

public class CapacityValidator {

    public static Mono<Capacity> validate(Capacity capacity) {
        return Mono.fromCallable(() -> {
            validateBasicFields(capacity);
            validateTechnologies(capacity);
            return capacity;
        });
    }

    public static Mono<Capacity> validateForSave(Capacity capacity) {
        return validate(capacity);
    }

    public static Mono<Capacity> validateTechnologyAddition(Capacity capacity, Long technologyId) {
        return Mono.fromCallable(() -> {
            if (capacity.getTechnologies() == null) {
                capacity.setTechnologies(new HashSet<>());
            }
            
            if (capacity.getTechnologies().size() >= 20) {
                throw new IllegalArgumentException(CapacityError.MAXIMUM_TECHNOLOGIES_EXCEEDED.getMessage());
            }
            
            if (capacity.getTechnologies().contains(technologyId)) {
                throw new IllegalArgumentException(CapacityError.TECHNOLOGY_ALREADY_EXISTS.getMessage());
            }
            
            return capacity;
        });
    }

    public static Mono<Capacity> validateTechnologyRemoval(Capacity capacity, Long technologyId) {
        return Mono.fromCallable(() -> {
            if (capacity.getTechnologies() == null || capacity.getTechnologies().size() <= 3) {
                throw new IllegalArgumentException(CapacityError.MINIMUM_TECHNOLOGIES_REQUIRED.getMessage());
            }
            return capacity;
        });
    }

    private static void validateBasicFields(Capacity capacity) {

        if (capacity.getName() == null || capacity.getName().trim().isEmpty()) {
            throw new IllegalArgumentException(CapacityError.CAPACITY_NAME_REQUIRED.getMessage());
        }
        
        if (capacity.getDescription() == null || capacity.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException(CapacityError.CAPACITY_DESCRIPTION_REQUIRED.getMessage());
        }
    }

    private static void validateTechnologies(Capacity capacity) {
        if (capacity.getTechnologies() == null) {
            capacity.setTechnologies(new HashSet<>());
        }

        Set<Long> uniqueTechnologies = new HashSet<>(capacity.getTechnologies());
        if (uniqueTechnologies.size() != capacity.getTechnologies().size()) {
            throw new IllegalArgumentException(CapacityError.DUPLICATE_TECHNOLOGIES.getMessage());
        }

        if (capacity.getTechnologies().size() < 3 || capacity.getTechnologies().size() > 20) {
            throw new IllegalArgumentException(CapacityError.INVALID_TECHNOLOGY_COUNT.getMessage());
        }
    }
}