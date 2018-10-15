package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DomainException extends RuntimeException {
    private CustomHttpStatus httpStatus;
    private String code;
    private String description;
    private String loggingDetails;

    public DomainException(CustomHttpStatus httpStatus, String code, String description) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
    }
}