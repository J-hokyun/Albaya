package com.example.albaya.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ExceptionDto {

    private String msg;
    private String detail;

    public static ResponseEntity<ExceptionDto> toResponseEntity(CustomException ex){
        ErrorCode errorCode = ex.getErrorCode();
        String detail = ex.getDetail();
        return ResponseEntity.status(errorCode.getStatus())
                .body(ExceptionDto.builder()
                        .msg(errorCode.getMessage())
                        .detail(detail)
                        .build());
    }
}
