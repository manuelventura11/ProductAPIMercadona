package com.mercadona.productapi.exception;

import com.mercadona.productapi.common.response.JSONResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class EANExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<JSONResult<Void>> handleConstraintViolationException(final ConstraintViolationException ex) {
        List<String> messages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String responseMessage = String.join(", ", messages);

        JSONResult<Void> result = new JSONResult<>(false, HttpStatus.BAD_REQUEST.value(), responseMessage, null);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}