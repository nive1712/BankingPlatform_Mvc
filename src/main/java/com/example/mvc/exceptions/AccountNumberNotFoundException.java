package com.example.mvc.exceptions;
public class AccountNumberNotFoundException extends RuntimeException {
    public AccountNumberNotFoundException(String message) {
        super(message);
    }

    public AccountNumberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}




