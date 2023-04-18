package ru.otus.spring.homework.oke.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ApplicationPropertiesProvider.class})
public class ApplicationConfig {
}
