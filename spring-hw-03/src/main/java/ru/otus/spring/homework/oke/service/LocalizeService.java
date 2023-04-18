package ru.otus.spring.homework.oke.service;

import java.net.URL;

public interface LocalizeService {
    String getMessage(String code);

    String getMessage(String code, Object[] args);

    URL getResourceUrl(String baseName, String suffix);
}
