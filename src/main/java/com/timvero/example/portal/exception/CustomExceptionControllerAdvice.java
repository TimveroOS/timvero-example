package com.timvero.example.portal.exception;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

import java.util.Map;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class CustomExceptionControllerAdvice {

    @ExceptionHandler(PreconditionFailedException.class)
    public ResponseEntity<Map<String, String>> handlePreconditionFailed(PreconditionFailedException exception) {
        return new ResponseEntity<>(
            Map.of("message", exception.getMessage()),
            responseHeaders(),
            HttpStatus.PRECONDITION_FAILED
        );
    }

    private HttpHeaders responseHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }
}