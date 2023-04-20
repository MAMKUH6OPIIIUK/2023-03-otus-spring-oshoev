package ru.otus.spring.homework.oke.dao;

public class TestingLoadingException extends RuntimeException {
    public TestingLoadingException() {
        super();
    }

    public TestingLoadingException(String message) {
        super(message);
    }

    public TestingLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
