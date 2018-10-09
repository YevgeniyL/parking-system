package com.parkingsystem.infrastructure.api.v1.management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewParkingLotApiRequest {
    private String url;
    private Boolean isEnabled;
}
