package com.parkingsystem.application.microservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagementMicroserviceTest {
    @Autowired
    private WebApplicationContext wac;

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    private MockMvc mockMvc;

    @Test
    void error_1001_test() throws Exception {
        NewParkingLotApiRequest request = new NewParkingLotApiRequest(null, true);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1001))
                .andExpect(jsonPath("error.message").value("Field 'address' is empty"));
    }

    @Test
    void error_1002_test() throws Exception {
        NewParkingLotApiRequest request = new NewParkingLotApiRequest("SomeAddr", null);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("error.status").value(422))
                .andExpect(jsonPath("error.errorCode").value(1002))
                .andExpect(jsonPath("error.message").value("Field 'isEnable' is empty"));
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
                .andExpect(status().isCreated());
    }
}
