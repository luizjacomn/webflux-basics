package com.luizjacomn.webfluxbasics.controller.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Mono<StandardError>> handleDuplicateKeyException(DuplicateKeyException ex, ServerHttpRequest request) {
        return ResponseEntity.badRequest()
                             .body(Mono.just(new StandardError(
                                 LocalDateTime.now(),
                                 request.getPath().toString(),
                                 HttpStatus.BAD_REQUEST.value(),
                                 HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                 this.duplicateKeyMessage(ex.getMessage())
                             )));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Mono<ValidationError>> handleValidationError(WebExchangeBindException ex, ServerHttpRequest request) {
        final var validationError = new ValidationError(
            LocalDateTime.now(),
            request.getPath().toString(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation error",
            "Error on validation attributes"
        );

        ex.getFieldErrors().forEach(fieldError -> validationError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage()));

        return ResponseEntity.badRequest()
                             .body(Mono.just(validationError));
    }

    private String duplicateKeyMessage(String exceptionMessage) {
        if (exceptionMessage.contains("email dup key")) {
            return "E-mail already registered";
        }

        return "Dup key exception";
    }

}
