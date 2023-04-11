package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Сервис потокового ввода-вывода ")
@ExtendWith(MockitoExtension.class)
public class IOServiceStreamsTest {
    IOService ioService;

    ByteArrayInputStream input;
    ByteArrayOutputStream output;

    @BeforeEach
    public void setUp() {
        String userInput = "Hello world!" + System.lineSeparator();
        input = new ByteArrayInputStream(userInput.getBytes(StandardCharsets.UTF_8));
        output = new ByteArrayOutputStream();
        ioService = new IOServiceStreams(input, output);
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
}
