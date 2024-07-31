package com.example.albaya.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StatusCode {
    //200
    TOKEN_VALID(HttpStatus.OK, ""),

    //400
    NOT_FOUND(HttpStatus.BAD_REQUEST, "요청하신 정보를 찾을 수 없습니다"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "입력값이 유효하지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 유효하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 유효하지 않습니다."),
    IMAGE_DUPLICATE(HttpStatus.BAD_REQUEST, "중복되는 이미지 입니다."),


    //401
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자 입니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다."),
    TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다. 다시 로그인 하여 주세요"),
    TOKEN_UNSUPPORTED (HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰 입니다."),

    //403
    ACCESS_DENIED(HttpStatus.FORBIDDEN,"접근이 거부되었습니다."),

    //404
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"요청한 데이터를 찾을 수 없습니다."),

    //409
    EMAIL_DUPLICATE(HttpStatus.CONFLICT, "이미 존재 하는 이메일 입니다"),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"서버 내부 오류입니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE,"서비스를 사용할 수 없습니다."),
    ;


    StatusCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    private final HttpStatus status;
    private final String message;
}
