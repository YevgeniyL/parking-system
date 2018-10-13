package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParkingError {
    IS_EMPTY_LICENSE_NUMBER_1001("Partial_Content_1001", "Field 'LicensePlateNumber' is empty"),
    PARKING_ADDRESS_IS_NOT_EXIST_1002("Internal_server_1002", "Parking address is not exist"),
    LICENSE_NUMBER_NOT_EXIST_1003("No_Content_1003", "License plate number not found in system. Please, sign up in parking system"),
    LICENSE_NUMBER_HAVE_OPEN_SESSION_1004("Internal_server_1004", "License plate number have open unpaid parking session in system. Please, call with administration"),
    USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005("No_Content_1005", "Not enough money on the balance for first parking"),
    CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006("No_Content_1006", "Not enough money on the balance in system to use credit for parking"),
    ;

    private String code;
    private String description;

    public void doThrow() {
        throw new DomainException(this.code, this.description);
    }
}
