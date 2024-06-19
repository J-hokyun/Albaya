package com.example.albaya.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ExceptionDto>handleCustom400Exception(CustomException ex){
        return ExceptionDto.toResponseEntity(ex);
    }
}
