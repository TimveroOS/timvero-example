package com.timvero.example.admin.procuring;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcuringType {

    public static final String CODE_PENALTY = "PENALTY";

    public static final com.timvero.application.procuring.ProcuringType PENALTY = new com.timvero.application.procuring.ProcuringType(CODE_PENALTY);
}
