package com.luizjacomn.webfluxbasics.model.request;

public record UserRequest(
    String name,
    String email,
    String password
) { }
