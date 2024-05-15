package com.dukez.best_travel.util.exceptions;

public class ForbiddenCustomerException extends RuntimeException {
    public ForbiddenCustomerException(String message) {
        super("This customer is blocked");
    }

}
