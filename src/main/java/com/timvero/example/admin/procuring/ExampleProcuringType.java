package com.timvero.example.admin.procuring;

import com.timvero.application.procuring.ProcuringType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleProcuringType {

    public static final String CODE_PENALTY = "PENALTY";

    public static final ProcuringType PENALTY = new ProcuringType(CODE_PENALTY);
}
