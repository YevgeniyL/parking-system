package com.parkingsystem.infrastructure.api.v1.pakingasset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewSessionApiRequest {
    private  String licensePlateNumber;
}
