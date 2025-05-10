package com.luizjacomn.webfluxbasics.controller.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class StandardError implements Serializable {

    @Serial
    private static final long serialVersionUID = 6877520052423482794L;

    private final LocalDateTime timestamp;

    private final String path;

    private final Integer status;

    private final String error;

    private final String message;

}
