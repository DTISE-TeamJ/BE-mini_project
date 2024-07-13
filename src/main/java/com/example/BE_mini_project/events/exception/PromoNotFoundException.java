package com.example.BE_mini_project.events.exception;

public class PromoNotFoundException extends RuntimeException {
    public PromoNotFoundException(String message) {
        super(message);
    }
}
