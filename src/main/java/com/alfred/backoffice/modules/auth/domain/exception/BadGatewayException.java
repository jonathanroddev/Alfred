package com.alfred.backoffice.modules.auth.domain.exception;

import org.springframework.http.HttpStatus;

public class BadGatewayException extends ApiException {

    public BadGatewayException(String code) {
        super(code);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_GATEWAY;
    }
}
