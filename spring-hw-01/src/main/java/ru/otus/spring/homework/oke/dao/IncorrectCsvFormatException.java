package ru.otus.spring.homework.oke.dao;

public class IncorrectCsvFormatException extends RuntimeException {
    public IncorrectCsvFormatException() {
        super();
    }

    public IncorrectCsvFormatException(String message) {
        super(message);
    }
}
