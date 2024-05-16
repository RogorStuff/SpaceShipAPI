package com.spaceshipapi.spaceshipapi.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

@Configuration
@Aspect
@Slf4j
public class ShipConfiguration {

    @Before("execution(* getShipId*(*))")
    public void trackNegativeIds() {
        log.warn("Received search with negative Id");
    }
}