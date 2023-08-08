package ru.otus.spring.homework.oke.service;

public interface KeyValueCacheService {
    void putString(String key, String value);

    String getString(String key);
}
