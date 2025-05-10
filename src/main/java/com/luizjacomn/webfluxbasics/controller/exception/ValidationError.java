package com.luizjacomn.webfluxbasics.controller.exception;

import lombok.Getter;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationError extends StandardError {

    @Serial
    private static final long serialVersionUID = 2693360908964090204L;

    private final List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationError(LocalDateTime timestamp, String path, Integer status, String error, String message) {
        super(timestamp, path, status, error, message);
    }

    public void addFieldError(String fieldName, String errorMessage) {
        this.fieldErrors.add(new FieldError(fieldName, errorMessage));
    }

}
