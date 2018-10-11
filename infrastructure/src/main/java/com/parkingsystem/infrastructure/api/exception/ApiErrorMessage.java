package com.parkingsystem.infrastructure.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
class ApiErrorMessage {
    private String description;
}
