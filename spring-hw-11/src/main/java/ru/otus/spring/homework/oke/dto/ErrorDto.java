package ru.otus.spring.homework.oke.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ErrorDto {
    private String message;

    private Map<String, List<String>> fields;

    private String stackTrace;

    public ErrorDto(String message) {
        this.message = message;
        this.fields = new HashMap<>();
    }

    public ErrorDto(String message, Map<String, List<String>> fields) {
        this.message = message;
        this.fields = fields;
    }

    public ErrorDto(String message, String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }
}
