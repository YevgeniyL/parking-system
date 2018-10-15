package com.parkingsystem.domain.errors;

import lombok.Getter;

@Getter
public enum CustomHttpStatus {
    EMPTY_FIELD(230),
    RESOURCE_NOT_FOUND(231),
    INTERNAL_SERVER_ERROR(232),
    BUSINESS_LOGIC(233),

    ;

    private Integer code = 500;

    CustomHttpStatus(Integer i) {
        this.code = i;
    }
}
