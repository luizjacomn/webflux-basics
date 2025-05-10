package com.luizjacomn.webfluxbasics.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name,
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    String email,
    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    String password
) { }
