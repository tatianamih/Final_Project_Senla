package com.courses.senla.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LimitPrecentException extends RuntimeException {

    public LimitPrecentException(String message, Double value) {
        super(message + value);
    }
}
