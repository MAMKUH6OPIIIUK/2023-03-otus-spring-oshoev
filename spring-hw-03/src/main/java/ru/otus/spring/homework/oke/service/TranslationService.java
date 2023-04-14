package ru.otus.spring.homework.oke.service;

public interface TranslationService {
    public String getTranslatedString(String code);

    public String getTranslatedString(String code, Object[] args);
}
