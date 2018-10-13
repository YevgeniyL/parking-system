package com.parkingsystem.domain.errors;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * All business logic errors
 */

@Getter
@AllArgsConstructor
public enum  BaseError {
    INTERNAL_SERV_ERROR("Internal_server", "Internal server error");
    private String code;
    private String description;
    public void doThrow() {
        throw new DomainException(this.code,this.description) ;
    }
}