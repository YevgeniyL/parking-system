package com.parkingsystem.infrastructure.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends DomainToHttpExceptionsConverter {
    public NoContentException(String errorMessage) {
        super(errorMessage);
    }
}
