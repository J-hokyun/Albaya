package com.example.albaya.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ExceptionDto {

    private String msg;
    private String detail;

    public static ResponseEntity<ExceptionDto> toResponseEntity(CustomException ex){
        StatusCode statusCode = ex.getStatusCode();
        String detail = ex.getDetail();
        return ResponseEntity.status(statusCode.getStatus())
                .body(ExceptionDto.builder()
                        .msg(statusCode.getMessage())
                        .detail(detail)
                        .build());
    }
}
