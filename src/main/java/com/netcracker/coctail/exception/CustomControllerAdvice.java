package com.netcracker.coctail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler({InvalidEmailOrPasswordException.class})
    public ResponseEntity<String> handleInvalidEmail(InvalidEmailOrPasswordException exception) {
        return new ResponseEntity<>("Invalid email or password", HttpStatus.EXPECTATION_FAILED);
    }
}
