package com.parkingsystem.infrastructure.api.exception;


import com.parkingsystem.domain.errors.DomainException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.parkingsystem.domain.errors.BaseError.INTERNAL_SERV_ERROR;

/**
 * Converter domain errors to HTTP statuses with description
 */

@Getter
@Slf4j
public class DomainToHttpExceptionsConverter extends RuntimeException {
    private String description;

    DomainToHttpExceptionsConverter(String errorMessage) {
        this.description = errorMessage;
    }

    public DomainToHttpExceptionsConverter(DomainException e) {
        log.error("Domain exception", e);
        if (e.getCode().equals("Internal_server")) throw new InternalServerError(e.getDescription());
        if (e.getCode().contains("Partial_Content")) throw new PartialContentException(e.getDescription());
        if (e.getCode().contains("No_Content")) throw new NoContentException(e.getDescription());
        throw new InternalServerError(e.getDescription());
    }

    public DomainToHttpExceptionsConverter(Exception e) {
        log.error("System exception", e);
        throw new InternalServerError(INTERNAL_SERV_ERROR.getDescription());
    }
}
