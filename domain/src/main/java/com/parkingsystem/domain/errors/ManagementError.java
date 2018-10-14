package com.parkingsystem.domain.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagementError {
    IS_EMPTY_ADDRESS_2001("Partial_Content_2001", "Field 'address' is empty"),
    IS_EMPTY_ENABLED_2002("Partial_Content_2002", "Field 'isEnable' is empty");

    private String code;
    private String description;

    public void doThrow() {
        throw new DomainException(this.code, this.description);
    }
}
