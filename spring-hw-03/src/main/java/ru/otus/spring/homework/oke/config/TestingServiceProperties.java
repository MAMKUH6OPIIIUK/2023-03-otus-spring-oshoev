package ru.otus.spring.homework.oke.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "testing.service")
public class TestingServiceProperties {
    private String theme;

    private Integer passingScore;

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setPassingScore(Integer passingScore) {
        this.passingScore = passingScore;
    }

    public String getTheme() {
        return theme;
    }

    public Integer getPassingScore() {
        return passingScore;
    }
}
