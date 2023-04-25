package ru.otus.spring.homework.oke.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.domain.Student;
import ru.otus.spring.homework.oke.service.IOService;
import ru.otus.spring.homework.oke.service.LocalizeService;
import ru.otus.spring.homework.oke.service.TestingService;

@ShellComponent
@RequiredArgsConstructor
public class TestingCLI {
    public static final String HELLO_MESSAGE_CODE = "testing.cli.hello";

    public static final String BYE_MESSAGE_CODE = "testing.cli.bye";

    private final TestingService testingService;

    private final IOService ioService;

    private final LocalizeService localizeService;

    private Student student;

    @ShellMethod(value = "Команда авторизации", key = {"li", "login"})
    public void login(@ShellOption(help = "Имя", defaultValue = "Анонимус") String name,
                      @ShellOption(help = "Фамилия", defaultValue = "Анонимусов") String surname) {
        this.student = new Student(name, surname);
        String helloMessage = this.localizeService.getMessage(HELLO_MESSAGE_CODE, new String[]{name, surname});
        this.ioService.printLine(helloMessage);
    }

    @ShellMethod(value = "Начать тестирование", key = {"st", "start testing"})
    @ShellMethodAvailability(value = "isStudentLoggedOn")
    public void startTesting() {
        testingService.executeStudentTesting(this.student);
    }

    @ShellMethod(value = "Команда завершения сессии", key = {"lo", "logout"})
    @ShellMethodAvailability(value = "isStudentLoggedOn")
    public void logout() {
        String byeMessage = this.localizeService.getMessage(BYE_MESSAGE_CODE);
        this.ioService.printLine(byeMessage);
        this.student = null;
    }

    private Availability isStudentLoggedOn() {
        String errorMessage = "Для выполнения команды сначала войдите в программу при помощи команды \"login\"";
        return this.student == null ? Availability.unavailable(errorMessage) : Availability.available();
    }
}
