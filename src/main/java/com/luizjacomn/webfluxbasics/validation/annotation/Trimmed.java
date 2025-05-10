package com.luizjacomn.webfluxbasics.validation.annotation;

import com.luizjacomn.webfluxbasics.validation.validator.TrimmedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = { TrimmedValidator.class })
@Target({ FIELD, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface Trimmed {

    String message() default "must not contain leading or trailing whitespace";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
