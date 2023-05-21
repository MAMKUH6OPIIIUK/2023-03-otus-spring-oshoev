package ru.otus.spring.homework.oke.shell;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.formatter.AuthorFormatter;
import ru.otus.spring.homework.oke.service.AuthorService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class AuthorCLI {
    private final AuthorService authorService;

    private final AuthorFormatter authorsFormatter;

    @ShellMethod(value = "Команда поиска всех существующих авторов книг", key = {"gaa", "get all authors"})
    public String getAllAuthors() {
        List<AuthorResponseDto> authors = this.authorService.findAll();
        return this.authorsFormatter.formatAuthors(authors, 0);
    }

    @ShellMethod(value = "Добавить нового автора", key = {"ca", "create author"})
    public String createAuthor(@ShellOption(help = "Имя автора", value = {"n", "name"})
                               @Size(min = 1, max = 255)
                                       String name,
                               @ShellOption(help = "Второе имя. Если есть", value = {"mn", "middleName"},
                                       defaultValue = ShellOption.NULL)
                               @Size(min = 0, max = 255)
                                       String middleName,
                               @ShellOption(help = "Отчество", value = {"p", "patronymic"},
                                       defaultValue = ShellOption.NULL)
                               @Size(min = 0, max = 255)
                                       String patronymic,
                               @ShellOption(help = "Фамилия", value = {"sn", "surname"})
                               @Size(min = 1, max = 255)
                                       String surname) {
        AuthorRequestDto requestDto = new AuthorRequestDto(name, middleName, patronymic, surname);
        AuthorResponseDto createdAuthor = this.authorService.create(requestDto);
        return "Успешно добавлен автор! " + this.authorsFormatter.formatAuthor(createdAuthor, 0);
    }

    @ShellMethod(value = "Удалить автора", key = {"ra", "remove author"})
    public void removeAuthor(@ShellOption(help = "Идентификатор автора") @Positive Long id) {
        this.authorService.deleteById(id);
    }
}
