package com.netcracker.coctail.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FOUND)
public class UserAlreadyAwaitsYourResponseException extends RuntimeException {
}
