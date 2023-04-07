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

    @DisplayName(" при вызове метода readStringWithPrompt должен записывать в output stream " +
            "приглашение без изменений и корректно читать из input stream строку")
    @Test
    public void correctReadStringWithPrompt() {
        String expectedPrompt = "Say hello";
        String expectedString = "Hello world!";

        String resultString = ioService.readStringWithPrompt(expectedPrompt);

        assertThat(resultString).isEqualTo(expectedString);
        assertThat(output.toString().trim()).isEqualTo(expectedPrompt);
    }

    @DisplayName(" при вызове метода outputString должен корректно записывать в output stream полученную строку")
    @Test
    public void correctOutputString() {
        String expectedString = "Hello world!";

        ioService.outputString(expectedString);

        assertThat(output.toString().trim()).isEqualTo(expectedString);
    }
}
