package ru.otus.spring.homework.oke.config;

import java.nio.charset.Charset;

public interface TestingDaoPropertiesProvider {
    String getResourceBasename();

    Charset getEncoding();
}
