package com.parkingsystem.application.controller.v1;

import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import com.parkingsystem.infrastructure.api.v1.pakingasset.CloseSessionApi;
import com.parkingsystem.infrastructure.api.v1.pakingasset.CloseSessionResponseApi;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1/assets")
@Slf4j
public class ParkingController {

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingTransformer transformer;

    @Transactional
    @PostMapping(path = "/{asset}/sessions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void createSession(@PathVariable("asset") String parkingAddress, @RequestBody NewSessionApiRequest request) {
        parkingService.createSession(ApiVersion.V1, transformer.toDomain(request), parkingAddress);
    }


    @Transactional
    @PostMapping(path = "/{asset}/vehicle/{licencePlateNumber}/session", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public CloseSessionResponseApi closeSession(@PathVariable("asset") String parkingAddress,
                                                @PathVariable("licencePlateNumber") String licencePlateNumber,
                                                @RequestBody CloseSessionApi request) {
        return transformer.toRest(parkingService.closeSession(ApiVersion.V1, transformer.toDomain(request), parkingAddress, licencePlateNumber, LocalDateTime.now()));
    }
}


