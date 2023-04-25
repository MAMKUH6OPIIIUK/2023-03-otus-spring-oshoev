package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.otus.spring.homework.oke.SpringHw04Application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис потокового ввода-вывода ")
@SpringBootTest(classes = {SpringHw04Application.class, IOServiceStreamsTest.TestConfig.class},
        properties = {"spring.main.allow-bean-definition-overriding=true"})
public class IOServiceStreamsTest {
    @Autowired
    IOService ioService;

    static ByteArrayInputStream input;
    static ByteArrayOutputStream output;

    @AfterEach
    public void resetOutput() {
        output.reset();
    }

    @DisplayName(" при вызове метода readLineWithPrompt должен записывать в output stream " +
            "приглашение без изменений и корректно читать из input stream строку")
    @Test
    public void correctReadLineWithPrompt() {
        String expectedPrompt = "Say hello";
        String expectedLine = "Hello world!";

        String resultString = ioService.readLineWithPrompt(expectedPrompt);

        assertThat(resultString).isEqualTo(expectedLine);
        assertThat(output.toString().trim()).isEqualTo(expectedPrompt);
    }

    @DisplayName(" при вызове метода printLine должен корректно записывать в output stream полученную строку")
    @Test
    public void correctPrintLine() {
        String expectedLine = "Hello world!";

        ioService.printLine(expectedLine);

        assertThat(output.toString().trim()).isEqualTo(expectedLine);
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        IOService IOServiceStreams() {
            String userInput = "Hello world!" + System.lineSeparator();
            input = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
            output = new ByteArrayOutputStream();
            return new IOServiceStreams(input, output);
        }
    }
}
