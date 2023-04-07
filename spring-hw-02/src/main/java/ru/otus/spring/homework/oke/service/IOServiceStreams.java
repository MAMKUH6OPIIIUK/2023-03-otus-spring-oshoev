package ru.otus.spring.homework.oke.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Класс реализует сервис для вывода и чтения строк в/из stdout и stdin
 */
@Service
public class IOServiceStreams implements IOService {

    private final Scanner input;

    private final PrintStream output;

    @Autowired
    public IOServiceStreams(@Value("#{T(System).in}") InputStream inputStream,
                            @Value("#{T(System).out}") OutputStream outputStream) {
        this.input = new Scanner(inputStream);
        this.output = new PrintStream(outputStream);
    }


    @Override
    public String readStringWithPrompt(String prompt) {
        outputString(prompt);
        return input.nextLine();
    }

    @Override
    public void outputString(String string) {
        output.println(string);
    }
}
