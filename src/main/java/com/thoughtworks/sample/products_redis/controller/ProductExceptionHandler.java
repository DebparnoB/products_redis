package com.thoughtworks.sample.products_redis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequest(HttpClientErrorException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(createExceptionResponseBody(
                                ex.getStatusText(),
                                ex.getClass().getCanonicalName(),
                                ex.getStatusCode().toString()
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createExceptionResponseBody(
                                "An unexpected error occurred",
                                ex.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR.toString()
                        )
                );
    }

    private Map<String, Object> createExceptionResponseBody(String message, String exMessage, String status) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("status", status);
        responseBody.put("exception", exMessage);
        return responseBody;
    }
}
