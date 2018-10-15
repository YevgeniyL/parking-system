package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagementError {
    IS_EMPTY_ADDRESS_2001(CustomHttpStatus.EMPTY_FIELD, "2001", "Field 'address' is empty"),
    IS_EMPTY_ENABLED_2002(CustomHttpStatus.EMPTY_FIELD, "2002", "Field 'isEnable' is empty");

    private CustomHttpStatus httpStatus;
    private String code;
    private String description;

    public void doThrow() {
        throw new DomainException(this.httpStatus, this.code, this.description);
    }
}
