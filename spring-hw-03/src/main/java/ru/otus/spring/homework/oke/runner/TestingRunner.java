package ru.otus.spring.homework.oke.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.service.TestingService;

@Component
public class TestingRunner implements CommandLineRunner {
    private final TestingService testingService;

    public TestingRunner(TestingService testingService) {
        this.testingService = testingService;
    }

    @Override
    public void run(String... args) throws Exception {
        testingService.executeStudentTesting();
    }
}
