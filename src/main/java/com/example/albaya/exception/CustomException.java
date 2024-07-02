package com.example.albaya.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final StatusCode statusCode;
    private final String detail;

    public CustomException(StatusCode statusCode){
        this.statusCode = statusCode;
        this.detail = "";
    }
}
