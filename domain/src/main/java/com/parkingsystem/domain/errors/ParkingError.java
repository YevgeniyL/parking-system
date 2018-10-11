package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ParkingError {
    IS_EMPTY_LICENSE_NUMBER_1001("Partial_Content_1001", "Field 'LicensePlateNumber' is empty"),
    IS_EMPTY_ADDRESS_1002("Partial_Content_1002", "Field 'Address' is empty"),
    LICENSE_NUMBER_NOT_EXIST_1003("No_Content_1003", "License plate number not found in system. Please, will sign up in parking system"),
    ;

    private String code;
    private String message;

    public void doThrow() {
        throw new DomainException(this.code, this.message);
    }
}
