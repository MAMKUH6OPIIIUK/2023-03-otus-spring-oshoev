package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;

public interface GenreFormatter {
    String formatGenre(Genre genre, int indent);

    String formatGenres(List<Genre> genres, int indent);
}
