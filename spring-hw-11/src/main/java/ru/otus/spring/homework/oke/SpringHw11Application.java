package ru.otus.spring.homework.oke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class SpringHw11Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringHw11Application.class);
    }
}
