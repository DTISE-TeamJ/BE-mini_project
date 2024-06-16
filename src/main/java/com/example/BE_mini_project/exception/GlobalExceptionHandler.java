package com.example.BE_mini_project.exception;

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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<CustomResponse<Object>> handleException(Exception ex) {
        CustomResponse<Object> response = new CustomResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<CustomResponse<Object>> handleNotFoundException(NoHandlerFoundException ex) {
        CustomResponse<Object> response = new CustomResponse<>(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(UsernameException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    public ResponseEntity<CustomResponse<Object>> handleUsernameNotFoundException(UsernameException ex) {
//        CustomResponse<Object> response = new CustomResponse<>(HttpStatus.UNAUTHORIZED, "Username Not Found", ex.getMessage(), null);
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(PasswordException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ResponseBody
//    public ResponseEntity<CustomResponse<Object>> handleIncorrectPasswordException(PasswordException ex) {
//        CustomResponse<Object> response = new CustomResponse<>(HttpStatus.UNAUTHORIZED, "Incorrect Password", ex.getMessage(), null);
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }
}