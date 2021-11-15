package com.netcracker.coctail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
public class InvalidEmailOrPasswordException extends RuntimeException {
}
