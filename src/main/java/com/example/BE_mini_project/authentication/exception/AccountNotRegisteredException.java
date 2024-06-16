package com.example.BE_mini_project.authentication.exception;

public class AccountNotRegisteredException extends RuntimeException {
    public AccountNotRegisteredException(String message) {
        super(message);
    }
}