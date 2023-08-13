package ru.otus.spring.homework.oke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.spring.homework.oke.service.QueenService;

@SpringBootApplication
public class SpringHw15Application {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringHw15Application.class, args);
        QueenService queenService = context.getBean(QueenService.class);
        queenService.startReproductionCycle();
    }
}
