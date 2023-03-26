package com.mercadona.productapi.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.mercadona.productapi.common.constants.UrlMvcConstants.*;

public class EANValidator implements ConstraintValidator<EAN, String> {

    @Override
    public void initialize(EAN constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            context.buildConstraintViolationWithTemplate(EAN_NULL_MESSAGE).addConstraintViolation();
            return false;
        }
        if (value.length() != 13) {
            context.buildConstraintViolationWithTemplate(EAN_LENGTH_MESSAGE).addConstraintViolation();
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                context.buildConstraintViolationWithTemplate(EAN_DIGITS_ONLY_MESSAGE).addConstraintViolation();
                return false;
            }
        }
        int lastDigit = Character.getNumericValue(value.charAt(value.length() - 1));
        if (lastDigit >= 1 && lastDigit <= 5) {
            // Producto destinado a tiendas Mercadona España
        } else if (lastDigit == 6) {
            // Producto destinado a tiendas Mercadona Portugal
        } else if (lastDigit == 8) {
            // Producto destinado a Almacenes
        } else if (lastDigit == 9) {
            // Producto destinado a Oficinas Mercadona
        } else if (lastDigit == 0) {
            // Producto destinado a Colmenas
        } else {
            return false;
        }
        return true;
    }
}