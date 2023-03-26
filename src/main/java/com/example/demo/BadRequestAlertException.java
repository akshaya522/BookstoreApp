package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.management.BadAttributeValueExpException;

public class BadRequestAlertException extends ResponseStatusException {
    public BadRequestAlertException(String errMsg) {
        super(HttpStatus.BAD_REQUEST, errMsg);
    }
}
