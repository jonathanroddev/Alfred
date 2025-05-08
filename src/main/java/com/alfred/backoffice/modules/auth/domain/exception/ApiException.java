package com.alfred.backoffice.modules.auth.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {
    private final String code;

    public ApiException(String code) {
        super();
        this.code = code;
    }

    public abstract HttpStatus getHttpStatus();
}