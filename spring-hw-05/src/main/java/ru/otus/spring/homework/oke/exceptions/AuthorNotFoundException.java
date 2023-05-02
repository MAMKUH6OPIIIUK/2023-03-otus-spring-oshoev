package ru.otus.spring.homework.oke.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
