package ru.otus.spring.homework.oke.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

@Setter
@ConfigurationProperties(prefix = "testing")
public class ApplicationPropertiesProvider implements LocaleProvider, TestingDaoPropertiesProvider,
        TestingServicePropertiesProvider {
    public static final String DAO_RESOURCE_PROPERTY = "resource-basename";

    public static final String DAO_ENCODING_PROPERTY = "encoding";

    public static final String TESTING_THEME_PROPERTY = "theme";

    public static final String TESTING_PASSING_SCORE_PROPERTY = "passing-score";

    private Locale locale;

    private Map<String, Object> dao;

    private Map<String, Object> service;

    @Override
    public Locale getLocale() {
        return this.locale;
    }

    @Override
    public String getResourceBasename() {
        return (String) this.dao.get(DAO_RESOURCE_PROPERTY);
    }

    @Override
    public Charset getEncoding() {
        return Charset.forName((String) this.dao.get(DAO_ENCODING_PROPERTY));
    }

    @Override
    public String getTheme() {
        return (String) this.service.get(TESTING_THEME_PROPERTY);
    }

    @Override
    public Integer getPassingScore() {
        return (Integer) this.service.get(TESTING_PASSING_SCORE_PROPERTY);
    }
}
