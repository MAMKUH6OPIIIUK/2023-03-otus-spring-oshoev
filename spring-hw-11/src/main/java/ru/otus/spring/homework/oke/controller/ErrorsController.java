package ru.otus.spring.homework.oke.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import ru.otus.spring.homework.oke.dto.ErrorDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ErrorsController {
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(NotFoundException ex) {
        ErrorDto responseBody = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(responseBody, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorDto> handleWebExchangeBindException(WebExchangeBindException ex) {
        Map<String, List<String>> fields = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            List<String> messagesForField = fields.get(fieldName);
            if (messagesForField == null) {
                messagesForField = new ArrayList<>();
                messagesForField.add(errorMessage);
                fields.put(fieldName, messagesForField);
            } else {
                messagesForField.add(errorMessage);
            }
        });
        ErrorDto responseBody = new ErrorDto("Validation error", fields);
        return ResponseEntity.badRequest().body(responseBody);
    }
}
