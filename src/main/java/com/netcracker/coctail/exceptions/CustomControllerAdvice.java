package com.netcracker.coctail.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

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
