package ru.otus.spring.homework.oke.service;

public interface LocalizeService {
    String getMessage(String code);

    String getMessage(String code, Object[] args);
}
