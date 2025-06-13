package com.timvero.example.admin;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(includeFilters = {@Filter({Controller.class, ControllerAdvice.class})}, useDefaultFilters = false)
@ConditionalOnWebApplication
public class CustomWebConfiguration {

}
