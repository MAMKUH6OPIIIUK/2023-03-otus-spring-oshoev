package ru.otus.spring.homework.oke.shell;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.formatter.GenreFormatter;
import ru.otus.spring.homework.oke.service.GenreService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class GenreCLI {
    private final GenreService genreService;

    private final GenreFormatter genreFormatter;

    @ShellMethod(value = "Получить все существующие жанры книг", key = {"gag", "get all genres"})
    public String getAllGenres() {
        List<GenreResponseDto> genres = this.genreService.findAll();
        return this.genreFormatter.formatGenres(genres, 0);
    }

    @ShellMethod(value = "Добавить новый жанр", key = {"cg", "create genre"})
    public String createGenre(@ShellOption(help = "Название жанра. Должно быть уникальным", value = {"n", "name"})
                              @Size(min = 1, max = 500)
                                      String name) {
        GenreRequestDto requestDto = new GenreRequestDto(name);
        GenreResponseDto createdGenre = this.genreService.create(requestDto);
        return "Успешно добавлен жанр! " + this.genreFormatter.formatGenre(createdGenre, 0);
    }

    @ShellMethod(value = "Удалить жанр", key = {"rg", "remove genre"})
    public void removeGenre(@ShellOption(help = "Идентификатор жанра") @Positive Long id) {
        this.genreService.deleteById(id);
    }
}
