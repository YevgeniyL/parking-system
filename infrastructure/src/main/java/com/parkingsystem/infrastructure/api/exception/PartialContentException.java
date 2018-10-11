package com.parkingsystem.infrastructure.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PARTIAL_CONTENT)
public class PartialContentException extends DomainToHttpExceptionsConverter {
    public PartialContentException(String errorMessage) {
        super(errorMessage);
    }
}
