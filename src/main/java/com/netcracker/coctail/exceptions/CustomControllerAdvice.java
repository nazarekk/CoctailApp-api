package com.netcracker.coctail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {
    @ExceptionHandler({InvalidEmailException.class})
    public ResponseEntity<String> handleInvalidEmail(InvalidEmailException exception) {
        return new ResponseEntity<>("Invalid Email Exception", HttpStatus.EXPECTATION_FAILED);
    }
    @ExceptionHandler({DuplicateEmailException.class})
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException exception) {
        return new ResponseEntity<>("Duplicate Email Exception", HttpStatus.FOUND);
    }
    @ExceptionHandler({InvalidPasswordException.class})
    public ResponseEntity<String> handleInvalidPassword(InvalidPasswordException exception) {
        return new ResponseEntity<>(" Invalid Password Exception", HttpStatus.EXPECTATION_FAILED);
    }

}