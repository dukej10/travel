package com.dukez.best_travel.api.controllers.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dukez.best_travel.api.models.response.BaseErrorResponse;
import com.dukez.best_travel.api.models.response.ErrorResponse;
import com.dukez.best_travel.util.exceptions.ForbiddenCustomerException;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenController {
    // Intercepta la excepción IdNotFoundException y devuelve el mensaje de error
    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleIdNotFound(ForbiddenCustomerException exception) {
        return ErrorResponse.builder().status(
                HttpStatus.FORBIDDEN.name()).errorCode(HttpStatus.FORBIDDEN.value())
                .message(exception.getMessage()).build();
    }
}
