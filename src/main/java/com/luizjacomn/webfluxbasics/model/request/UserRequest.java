package com.luizjacomn.webfluxbasics.model.request;

import com.luizjacomn.webfluxbasics.validation.annotation.Trimmed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @Trimmed
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    String name,

    @Trimmed
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    String email,

    @Trimmed
    @NotBlank(message = "Password is required")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters")
    String password
) { }
