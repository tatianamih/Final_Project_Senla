package com.courses.senla.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceRepetitionException extends RuntimeException {
    public ResourceRepetitionException(String message) {
        super(message);
    }

    public ResourceRepetitionException(String message, String name) {
        super(message + name);
    }
}
