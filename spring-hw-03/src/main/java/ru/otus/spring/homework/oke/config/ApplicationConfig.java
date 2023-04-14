package ru.otus.spring.homework.oke.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({TestingDaoProperties.class, TestingServiceProperties.class,
        TestingLocalizationProperties.class})
public class ApplicationConfig {
}
