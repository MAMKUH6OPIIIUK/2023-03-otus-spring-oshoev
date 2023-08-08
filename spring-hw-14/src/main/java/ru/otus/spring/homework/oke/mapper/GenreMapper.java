package ru.otus.spring.homework.oke.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.document.GenreDocument;
import ru.otus.spring.homework.oke.model.entity.Genre;

@Component
public class GenreMapper {
    public GenreDocument mapToGenreDocument(Genre genre) {
        return new GenreDocument(genre.getName());
    }
}
