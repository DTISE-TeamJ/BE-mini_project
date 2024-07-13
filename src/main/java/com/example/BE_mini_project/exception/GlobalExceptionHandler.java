package com.example.BE_mini_project.exception;

import com.example.BE_mini_project.events.exception.EventNotFoundException;
import com.example.BE_mini_project.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleEventNotFoundException(EventNotFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage(),
                null
        );
        return response.toResponseEntity();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CustomResponse<Object>> handleNotFoundException(NoHandlerFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(
                HttpStatus.NOT_FOUND,
                "Not Found",
                ex.getMessage(),
                null
        );
        return response.toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<Object>> handleException(Exception ex) {
        CustomResponse<Object> response = new CustomResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Server Error",
                ex.getMessage(),
                null
        );
        return response.toResponseEntity();
    }
}