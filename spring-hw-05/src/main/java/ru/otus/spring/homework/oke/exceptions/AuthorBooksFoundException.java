package ru.otus.spring.homework.oke.exceptions;

public class AuthorBooksFoundException extends RuntimeException {
    public AuthorBooksFoundException(String message) {
        super(message);
    }
}
