package com.courses.senla.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NegativeAccountException extends RuntimeException {

    public NegativeAccountException(String message) {
        super(message);
    }
}
