package com.dukez.best_travel.util.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User no exist in %s";

    public UsernameNotFoundException(String collectionName) {
        super(String.format(ERROR_MESSAGE, collectionName));
    }
}
