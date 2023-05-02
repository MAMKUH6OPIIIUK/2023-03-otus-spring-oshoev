package ru.otus.spring.homework.oke.exceptions;

public class NonUniqueGenreException extends RuntimeException {
    public NonUniqueGenreException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
