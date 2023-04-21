package ru.otus.spring.homework.oke.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.util.Locale;

@Setter
@Getter
@ConfigurationProperties(prefix = "testing")
public class ApplicationPropertiesProvider implements LocaleProvider, TestingDaoPropertiesProvider,
        TestingServicePropertiesProvider {

    private Locale locale;

    private String resourceName;

    private Charset encoding;

    private String theme;

    private int passingScore;

    @Override
    public Locale getLocale() {
        return this.locale;
    }
}
