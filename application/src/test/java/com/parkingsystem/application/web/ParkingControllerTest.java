package com.parkingsystem.application.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.application.controller.v1.ParkingControllerV1;
import com.parkingsystem.domain.sevice.parking.ParkingService;
import com.parkingsystem.infrastructure.api.v1.pakingasset.NewSessionApiRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@MockBean(ParkingService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParkingControllerTest {

    @Autowired
    private ParkingControllerV1 parkingControllerV1;
    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(parkingControllerV1).build();
    }


    @Test
    @DisplayName("Test Parking controller v1")
    void responseTest() throws Exception {
        NewSessionApiRequest request = new NewSessionApiRequest("123xxxx");
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(
                MockMvcRequestBuilders.post("/pms/v1/assets/1/sessions")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}