package com.luizjacomn.webfluxbasics.controller.exception;

import java.time.LocalDateTime;

public record StandardError(
    LocalDateTime timestamp,
    String path,
    Integer status,
    String error,
    String message
) {
}
