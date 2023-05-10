package ru.otus.spring.homework.oke.mappers;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

@Component
public class GenreMapper {
    public Genre mapToGenre(GenreRequestDto genreRequestDto) {
        Genre genre = new Genre(genreRequestDto.getName());
        return genre;
    }

    public GenreResponseDto mapToGenreResponseDto(Genre genre) {
        GenreResponseDto dto = new GenreResponseDto(genre.getId(), genre.getName());
        return dto;
    }
}
