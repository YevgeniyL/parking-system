package com.parkingsystem.application.controller.v1;

import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import com.parkingsystem.infrastructure.api.exception.DomainToHttpExceptionsConverter;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pms/v1")
@Slf4j
public class ParkingController {

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingTransformer transformer;

    @Transactional
    @PostMapping(path = "/assets/{asset}/sessions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void createSession(@PathVariable("asset") String parkingAddress, @RequestBody NewSessionApiRequest request) {
        try {
            parkingService.createSession(ApiVersion.V1, transformer.toDomain(request), parkingAddress);
        } catch (DomainException e) {
            log.error("Domain exception", e);
            throw new DomainToHttpExceptionsConverter(e);
        }
    }

//    POST https://$host/pms/v1/assets/$asset/sessions

//    POST  https://$host/pms/v1/assets/$asset/vehicle/$licencePlateNumber/session

}
