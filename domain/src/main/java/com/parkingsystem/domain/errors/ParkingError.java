package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParkingError {
    //Open session
    IS_EMPTY_LICENSE_NUMBER_1001(CustomHttpStatus.EMPTY_FIELD, "1001", "Field 'LicensePlateNumber' is empty"),
    PARKING_ADDRESS_IS_NOT_EXIST_1002(CustomHttpStatus.RESOURCE_NOT_FOUND, "1002", "Parking address does not exist"),
    LICENSE_NUMBER_NOT_EXIST_1003(CustomHttpStatus.RESOURCE_NOT_FOUND, "1003", "License plate number not found in system. Please, sign up in parking system"),
    LICENSE_NUMBER_HAVE_OPEN_SESSION_1004(CustomHttpStatus.INTERNAL_SERVER_ERROR, "1004", "License plate number have open unpaid parking session in system. Please, call with administration"),
    USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005(CustomHttpStatus.BUSINESS_LOGIC, "1005", "Not enough money on the balance for first parking"),
    CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006(CustomHttpStatus.BUSINESS_LOGIC, "1006", "Not enough money on the balance in system to use credit for parking"),
    PARKING_LOT_IS_NOT_WORKING_1007(CustomHttpStatus.RESOURCE_NOT_FOUND, "1007", "Parking address does not exist"),

    //Close session
    IS_EMPTY_STATUS_1051(CustomHttpStatus.EMPTY_FIELD, "1051", "Field 'status' is empty"),
    IS_NOT_STOPPED_STATUS_1053(CustomHttpStatus.BUSINESS_LOGIC, "1052", "Status for closing session is not a 'stopped'"),
    PARKING_ADDRESS_IS_NOT_EXIST_1054(CustomHttpStatus.RESOURCE_NOT_FOUND, "1053", "Parking address is not exist"),
    LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055(CustomHttpStatus.INTERNAL_SERVER_ERROR, "1055", "License plate number do not have parking session in system for this user. Please, call with administration or setup valid number in account or on a car"),

    ;
    private CustomHttpStatus httpStatus;
    private String code;
    private String description;

    public void doThrow() {
        throw new DomainException(this.httpStatus, this.code, this.description);
    }
}
