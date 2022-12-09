package com.courses.senla.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Long id) {
        super(message + id);
    }

    public ResourceNotFoundException(String message, String value) {
        super(message + value);
    }

    public ResourceNotFoundException(String message, Date date) {
        super(message + date);
    }

    public ResourceNotFoundException(String message1, Long id, String message2,String login) {
        super(message1 + id + message2 + login);
    }
}
