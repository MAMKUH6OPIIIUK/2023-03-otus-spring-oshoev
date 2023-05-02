package ru.otus.spring.homework.oke.exceptions;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
