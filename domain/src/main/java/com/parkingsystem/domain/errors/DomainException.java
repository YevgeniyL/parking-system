package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DomainException extends RuntimeException {
    private String code;
    private String description;
}