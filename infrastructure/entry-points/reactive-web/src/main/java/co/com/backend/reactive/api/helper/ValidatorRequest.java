package co.com.backend.reactive.api.helper;

import co.com.backend.reactive.api.dtos.request.CapacityRequestDTO;
import co.com.backend.reactive.api.exceptions.BusinessExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidatorRequest {

    private final Validator validator;

    public void validateDTO(CapacityRequestDTO dto) {
        Set<ConstraintViolation<CapacityRequestDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            throw new BusinessExceptionHandler(violations);
        }
    }
}
