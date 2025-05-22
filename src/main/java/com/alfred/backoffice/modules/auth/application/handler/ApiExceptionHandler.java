package com.alfred.backoffice.modules.auth.application.handler;

import com.alfred.backoffice.modules.auth.domain.exception.ApiException;
import com.alfred.backoffice.modules.auth.infrastructure.configuration.ErrorMessageProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final ErrorMessageProperties errorMessages;

    public ApiExceptionHandler(ErrorMessageProperties errorMessages) {
        this.errorMessages = errorMessages;
    }

    private ResponseEntity<Map<String, String>> getAlfredResponseEntity(HttpStatus httpStatus, String code, String message) {
        return ResponseEntity
                .status(httpStatus)
                .body(Map.of(
                        "code", code,
                        "message", message
                ));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApiException(ApiException ex) {
        String code = ex.getCode();
        String message = errorMessages.getMessage(code);
        return this.getAlfredResponseEntity(ex.getHttpStatus(), code, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String code = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        assert code != null;
        String message = errorMessages.getMessage(code);
        return this.getAlfredResponseEntity(HttpStatus.BAD_REQUEST, code, message);
    }
}