package com.parkingsystem.application.controller.v1;

import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import com.parkingsystem.infrastructure.api.exception.ApiException;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import com.parkingsystem.infrastructure.api.v1.pakingasset.ParkingTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pms/v1")
public class ParkingControllerV1 {
    private static Logger logger = LoggerFactory.getLogger(ParkingControllerV1.class);

    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ParkingTransformer transformer;

    @Transactional
    @PostMapping(path = "/assets/{asset}/sessions", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createSession(@PathVariable("asset") int parkingId, @RequestBody NewSessionApiRequest request) {
        try {
            parkingService.createSession(ApiVersion.V1, transformer.toDomain(request), parkingId);
        } catch (DomainException e) {
            logger.error("Domain exception", e);
            throw new ApiException(e);
        }
    }

//    POST https://$host/pms/v1/assets/$asset/sessions

//    POST  https://$host/pms/v1/assets/$asset/vehicle/$licencePlateNumber/session

}
