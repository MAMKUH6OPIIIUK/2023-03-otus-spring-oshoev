package ru.otus.spring.homework.oke.formatter;

import ru.otus.spring.homework.oke.dto.GenreResponseDto;

import java.util.List;

public interface GenreFormatter {
    String formatGenre(GenreResponseDto genre, int indent);

    String formatGenres(List<GenreResponseDto> genres, int indent);
}
