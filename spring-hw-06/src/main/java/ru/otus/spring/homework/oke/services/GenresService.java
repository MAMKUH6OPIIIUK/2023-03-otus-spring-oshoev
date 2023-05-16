package ru.otus.spring.homework.oke.services;

import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

import java.util.List;

public interface GenresService {
    GenreResponseDto create(GenreRequestDto genreRequestDto);

    GenreResponseDto findById(Long id);

    List<GenreResponseDto> findAll();

    void deleteById(Long id);
}
