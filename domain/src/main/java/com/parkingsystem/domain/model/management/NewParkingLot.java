package com.parkingsystem.domain.model.management;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewParkingLot {
    private String address;
    private Boolean isEnabled;
}
