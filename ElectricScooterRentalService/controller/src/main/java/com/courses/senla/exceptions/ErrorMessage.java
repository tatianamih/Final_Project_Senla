package com.courses.senla.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ErrorMessage {

    private Date timestamp;

    private String message;

    private String details;

    private HttpStatus httpStatus;

    private int code;

    public ErrorMessage(Date timestamp, String message, String details, HttpStatus httpStatus, int code) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.httpStatus = httpStatus;
        this.code = code;
    }
}