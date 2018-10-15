package com.parkingsystem.application.controller.v1;

import com.parkingsystem.domain.sevice.ApiVersion;
import com.parkingsystem.domain.sevice.management.ManagementService;
import com.parkingsystem.infrastructure.api.v1.management.ManagementTransformer;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pms/v1/management/")
@Slf4j
public class ManagementController {

    @Autowired
    private ManagementTransformer transformer;
    @Autowired
    private ManagementService managementService;

    @Transactional
    @PostMapping(path = "parkinglots", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestBody NewParkingLotApiRequest request) {
        managementService.save(ApiVersion.V1, transformer.toDomain(request));
    }
}
