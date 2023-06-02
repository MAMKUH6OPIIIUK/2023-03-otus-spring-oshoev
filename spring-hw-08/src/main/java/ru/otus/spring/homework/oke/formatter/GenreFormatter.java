package ru.otus.spring.homework.oke.formatter;

import ru.otus.spring.homework.oke.dto.GenreDto;

import java.util.List;

public interface GenreFormatter {
    String formatGenre(GenreDto genre, int indent);

    String formatGenres(List<GenreDto> genres, int indent);
}
