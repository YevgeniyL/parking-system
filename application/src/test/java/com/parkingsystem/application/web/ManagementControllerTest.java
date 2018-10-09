package com.parkingsystem.application.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingsystem.application.controller.ManagementV1Controller;
import com.parkingsystem.domain.sevice.management.ManagementService;
import com.parkingsystem.infrastructure.api.v1.management.NewParkingLotApiRequest;
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
@MockBean(ManagementService.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManagementControllerTest {

    @Autowired
    private ManagementV1Controller managementV1Controller;
    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(managementV1Controller).build();
    }


    @Test
    @DisplayName("Test management v1 controller")
    void responseTest() throws Exception {

        NewParkingLotApiRequest request = new NewParkingLotApiRequest("SomeURL", true);
        String body = (new ObjectMapper()).valueToTree(request).toString();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/pms/v1/management/parkinglots")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
