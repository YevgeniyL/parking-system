package com.parkingsystem.infrastructure.api.exception;


import com.parkingsystem.domain.errors.DomainException;
import lombok.Getter;

/**
 * Converter domain errors to HTTP statuses with description
 */

@Getter
public class DomainToHttpExceptionsConverter extends RuntimeException {
    private String description;

    DomainToHttpExceptionsConverter(String errorMessage) {
        this.description = errorMessage;
    }

    public DomainToHttpExceptionsConverter(DomainException e) {
        if (e.getCode().equals("Internal_server")) throw new InternalServerError(e.getMessage());
        if (e.getCode().contains("Partial_Content")) throw new PartialContentException(e.getMessage());
        if (e.getCode().contains("No_Content")) throw new NoContentException(e.getMessage());
        throw new InternalServerError(e.getMessage());
    }
}
