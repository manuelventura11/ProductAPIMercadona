package com.mercadona.productapi.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EANValidator.class)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EAN {
    String message() default "Código de barras EAN inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
