package ru.otus.spring.homework.oke.shell;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class ApplicationCLI {
    @ShellMethod(value = "Включить консоль H2", key = {"eh2", "enable h2 console"})
    public String enableH2() {
        String result;
        try {
            Console.main();
            result = "Консоль включена!";
        } catch (Exception e) {
            result = "Не удалось включить консоль. Ошибка: " + e.getMessage();
        }
        return result;
    }
}
