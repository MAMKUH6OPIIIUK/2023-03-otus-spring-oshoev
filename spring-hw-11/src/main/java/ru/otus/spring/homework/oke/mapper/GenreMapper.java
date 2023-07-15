package ru.otus.spring.homework.oke.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.dto.GenreDto;

@Component
public class GenreMapper {
    public Genre mapToGenre(GenreDto genreDto) {
        return new Genre(genreDto.getName());
    }

    public GenreDto mapToGenreDto(Genre genre) {
        return new GenreDto(genre.getName());
    }
}
