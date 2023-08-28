package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

import java.util.List;

public interface GenreService {
    GenreResponseDto create(GenreRequestDto genreRequestDto);

    GenreResponseDto findById(Long id);

    List<GenreResponseDto> findAll();

    void deleteById(Long id);
}
