package com.luizjacomn.webfluxbasics.validation.validator;

import com.luizjacomn.webfluxbasics.validation.annotation.Trimmed;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedValidator implements ConstraintValidator<Trimmed, String> {

    @Override
    public void initialize(Trimmed constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.trim().length() == value.length();
    }

}
