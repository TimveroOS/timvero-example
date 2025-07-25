package com.timvero.example.portal.ping;

import static com.timvero.example.portal.InternalApiSecurityConfig.BASIC_AUTH;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("process")
@SecurityRequirement(name = BASIC_AUTH)
public class InternalApiController {
    @GetMapping("ping")
    public String ping() {
        return "pong";
    }
}
