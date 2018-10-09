package com.parkingsystem.domain.errors;


import lombok.Getter;


/**
 * All business logic errors
 */

@Getter
public enum ErrorMessages {
    DOMAIN_ERROR_1001(1001, "Field 'url' is empty"),
    DOMAIN_ERROR_1002(1002, "Field 'isEnable' is empty"),


    DOMAIN_ERROR_E9999(9999, "Internal server error"),
    ;

    private int code;
    private String message;

    ErrorMessages(int code, String message) {
        this.code = code;
        this.message = message;
    }
}