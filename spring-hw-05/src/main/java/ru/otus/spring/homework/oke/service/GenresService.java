package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

import java.util.List;

public interface GenresService {
    GenreResponseDto create(GenreRequestDto genreRequestDto);

    GenreResponseDto findById(long id);

    List<GenreResponseDto> findAll();

    void deleteById(long id);
}
