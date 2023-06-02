package ru.otus.spring.homework.oke.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.homework.oke.dto.GenreDto;
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
        List<GenreDto> genres = this.genreService.findAll();
        return this.genreFormatter.formatGenres(genres, 0);
    }
}
