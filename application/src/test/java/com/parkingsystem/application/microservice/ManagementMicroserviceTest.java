package com.parkingsystem.application.microservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.domain.errors.ManagementError;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class ManagementMicroserviceTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void error_2001_test() throws Exception {
        NewParkingLotApiRequest request = new NewParkingLotApiRequest(null, true);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(ManagementError.IS_EMPTY_ADDRESS_2001.getHttpStatus().getCode()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ManagementError.IS_EMPTY_ADDRESS_2001.getDescription()));
    }

    @Test
    void error_2002_test() throws Exception {
        NewParkingLotApiRequest request = new NewParkingLotApiRequest("SomeAddr", null);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(ManagementError.IS_EMPTY_ENABLED_2002.getHttpStatus().getCode()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("description").value(ManagementError.IS_EMPTY_ENABLED_2002.getDescription()));
    }

    @Test
    @Transactional
    @Rollback
    void validTest() throws Exception {
        NewParkingLotApiRequest request = new NewParkingLotApiRequest("SomeAddr", true);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
}
