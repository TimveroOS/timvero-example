package com.timvero.example.portal;

import com.timvero.api.config.ApiWebConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {ApiWebConfig.class, PortalWebConfiguration.class})
public class PortalWebConfiguration {

}
