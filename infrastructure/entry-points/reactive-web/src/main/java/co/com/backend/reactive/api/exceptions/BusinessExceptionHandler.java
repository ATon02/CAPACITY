package co.com.backend.reactive.api.exceptions;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

public class BusinessExceptionHandler extends ConstraintViolationException {

    public BusinessExceptionHandler(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }
}
