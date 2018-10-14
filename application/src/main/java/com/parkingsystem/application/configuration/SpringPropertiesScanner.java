package com.parkingsystem.application.configuration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpringPropertiesScanner implements ApplicationContextInitializer {
    private final List<String> systemProps = Arrays.asList(
            "price.tariff",
            "price.roundInterval",
            "price.minimalAmount",
            "price.minimalAmountForCredit");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Set<String> notFoundSet = systemProps
                .stream()
                .filter(prop -> !applicationContext.getEnvironment().containsProperty(prop))
                .collect(Collectors.toSet());

        if (notFoundSet.size() > 0) {
            throw new RuntimeException("\nAny variables need to setup in application.properties: \n" + StringUtils.arrayToDelimitedString(notFoundSet.toArray(), ", \n"));
        }
    }
}
