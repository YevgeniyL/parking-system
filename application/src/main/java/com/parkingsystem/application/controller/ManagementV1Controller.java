package com.parkingsystem.application.controller;

import com.parkingsystem.domain.errors.DomainException;
import com.parkingsystem.domain.sevice.management.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import com.parkingsystem.infrastructure.api.exception.ApiException;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
import com.parkingsystem.infrastructure.api.v1.management.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pms/v1/management/")
public class ManagementV1Controller {
    private static Logger logger = LoggerFactory.getLogger(ManagementV1Controller.class);

    @Autowired
    private Transformer transformer;
    @Autowired
    private ManagementService managementService;

    @Transactional
    @PostMapping(path = "parkinglots",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody NewParkingLotApiRequest request) {
        try {
            managementService.save(transformer.toDomain(request), ApiVersion.V1);
        } catch (DomainException e) {
            logger.error("Domain exception", e);
            throw new ApiException(e);
        }
    }
}
