package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParkingError {
    //Open session
    IS_EMPTY_LICENSE_NUMBER_1001("Partial_Content_1001", "Field 'LicensePlateNumber' is empty"),
    PARKING_ADDRESS_IS_NOT_EXIST_1002("Internal_server_1002", "Parking address is not exist"),
    LICENSE_NUMBER_NOT_EXIST_1003("No_Content_1003", "License plate number not found in system. Please, sign up in parking system"),
    LICENSE_NUMBER_HAVE_OPEN_SESSION_1004("Internal_server_1004", "License plate number have open unpaid parking session in system. Please, call with administration"),
    USER_BALANCE_TOO_LOW_FOR_OPEN_SESSION_1005("No_Content_1005", "Not enough money on the balance for first parking"),
    CREDIT_LIMIT_TO_BIG_FOR_OPEN_SESSION_1006("No_Content_1006", "Not enough money on the balance in system to use credit for parking"),
    PARKING_LOT_IS_NOT_WORKING_1007("No_Content_1007", "Parking address is not exist"),

    //Close session
    IS_EMPTY_STATUS_1051("Internal_server_1051", "Field 'status' is empty"),
    IS_NOT_STOPPED_STATUS_1053("Internal_server_1052", "Status for closing session is not a 'stopped'"),
    PARKING_ADDRESS_IS_NOT_EXIST_1054("Internal_server_1053", "Parking address is not exist"),
    LICENSE_NUMBER_NO_HAVE_OPEN_SESSION_1055("Internal_server_1055", "License plate number do not have parking session in system for this user. Please, call with administration or setup valid number in account or on a car"),

    ;
    private String code;
    private String description;

    public void doThrow() {
        throw new DomainException(this.code, this.description);
    }
}
