package com.dukez.best_travel.api.controllers.error_handler;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dukez.best_travel.api.models.response.BaseErrorResponse;
import com.dukez.best_travel.api.models.response.ErrorResponse;
import com.dukez.best_travel.api.models.response.ErrorsResponse;
import com.dukez.best_travel.util.exceptions.IdNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    // Intercepta la excepción IdNotFoundException y devuelve el mensaje de error
    @ExceptionHandler(IdNotFoundException.class)
    public BaseErrorResponse handleIdNotFound(IdNotFoundException exception) {
        return ErrorResponse.builder().status(
                HttpStatus.BAD_REQUEST.name()).errorCode(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage()).build();
    }

    /**
     * Intercepta la excepción MethodArgumentNotValidException de @Valid
     * y estructura el mensaje de error
     * 
     * @param exception MethodArgumentNotValidException generada por @Valid
     * @return ResponseEntity<Object> con el mensaje de error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        var errors = new ArrayList<String>();
        exception.getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ErrorsResponse.builder().status(
                HttpStatus.BAD_REQUEST.name()).errorCode(HttpStatus.BAD_REQUEST.value())
                .message(errors).build();
    }
}
