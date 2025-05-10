package com.luizjacomn.webfluxbasics.controller.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public final class FieldError implements Serializable {

    @Serial
    private static final long serialVersionUID = 5469057845794626721L;

    private final String fieldName;

    private final String message;

}
