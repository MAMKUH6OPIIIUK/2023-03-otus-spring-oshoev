package ru.otus.spring.homework.oke;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.homework.oke.service.TestingService;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestingService testingService = context.getBean(TestingService.class);
        testingService.executeStudentTesting("Java testing from CSV", 40);
        context.close();
    }
}
