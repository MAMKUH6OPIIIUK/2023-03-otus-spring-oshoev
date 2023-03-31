package ru.otus.spring.homework.oke.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Класс реализует сервис для вывода и чтения строк в/из stdout и stdin
 */
public class IOServiceCLI implements IOService {

    private final Scanner input;

    private final PrintStream output;

    public IOServiceCLI(InputStream inputStream, OutputStream outputStream) {
        this.input = new Scanner(inputStream);
        this.output = new PrintStream(outputStream);
    }

    @Override
    public void printLine(String line) {
        this.output.println(line);
    }

    @Override
    public String readLine() {
        return this.input.nextLine();
    }
}
