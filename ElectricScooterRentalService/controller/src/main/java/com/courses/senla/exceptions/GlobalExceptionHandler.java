package com.courses.senla.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundHandling(ResourceNotFoundException exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                exception.getMessage()
                , request.getDescription(false)
                , NOT_FOUND
                , NOT_FOUND.value());
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

    @ExceptionHandler(ResourceRepetitionException.class)
    public ResponseEntity<?> ResourceDuplicationException(ResourceRepetitionException exception, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(),
                exception.getMessage()
                , request.getDescription(false)
                , CONFLICT
                , CONFLICT.value());
        return new ResponseEntity<>(errorMessage, CONFLICT);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> AuthenticationExceptionHandling(AuthenticationException exception, WebRequest request) {
        ErrorMessage errorMessage =
                new ErrorMessage(new Date()
                        , exception.getMessage()
                        , request.getDescription(false)
                        , FORBIDDEN
                        , FORBIDDEN.value());
        return new ResponseEntity<>(errorMessage, FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandling(Exception exception, WebRequest request) {
        ErrorMessage errorMessage =
                new ErrorMessage(new Date()
                        , exception.getMessage()
                        , request.getDescription(false)
                        , INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorMessage, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> UsernameNotFoundExceptionHandling(UsernameNotFoundException exception, WebRequest request) {
        ErrorMessage errorMessage =
                new ErrorMessage(new Date()
                        , exception.getMessage()
                        , request.getDescription(false)
                        , NOT_FOUND
                        , NOT_FOUND.value());
        return new ResponseEntity<>(errorMessage, NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
