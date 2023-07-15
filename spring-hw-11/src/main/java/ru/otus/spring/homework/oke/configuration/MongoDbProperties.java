package ru.otus.spring.homework.oke.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConditionalOnProperty(name = "mongock.enabled")
@ConfigurationProperties(prefix = "spring.data.mongodb")
@RequiredArgsConstructor
public class MongoDbProperties {
    private final String database;
}
