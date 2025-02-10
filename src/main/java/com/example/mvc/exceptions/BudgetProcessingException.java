package com.example.mvc.exceptions; 

public class BudgetProcessingException extends RuntimeException {
    public BudgetProcessingException(String message) {
        super(message);
    }

    public BudgetProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
