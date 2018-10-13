package com.parkingsystem.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@TestConfiguration
public class RestConfiguration {

    @Autowired
    private WebApplicationContext wac;

    @Bean
    public MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
