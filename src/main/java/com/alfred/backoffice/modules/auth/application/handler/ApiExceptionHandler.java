package com.alfred.backoffice.modules.auth.application.handler;

import com.alfred.backoffice.modules.auth.domain.exception.ApiException;
import com.alfred.backoffice.modules.auth.infrastructure.configuration.ErrorMessageProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final ErrorMessageProperties errorMessages;

    public ApiExceptionHandler(ErrorMessageProperties errorMessages) {
        this.errorMessages = errorMessages;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException ex) {
        String code = ex.getCode();
        String message = errorMessages.getMessage(code);

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(Map.of(
                        "code", code,
                        "message", message
                ));
    }
}