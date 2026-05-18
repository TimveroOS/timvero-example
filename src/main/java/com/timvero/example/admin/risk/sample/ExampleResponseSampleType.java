package com.timvero.example.admin.risk.sample;

import com.timvero.loan.response_sample.entity.ResponseSampleType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExampleResponseSampleType {

    public static final String APPLICATION_VALUE = "APPLICATION";

    public static final ResponseSampleType DOCUMENT = ResponseSampleType.DOCUMENT;
    public static final ResponseSampleType APPLICATION = new ResponseSampleType(APPLICATION_VALUE);
}
