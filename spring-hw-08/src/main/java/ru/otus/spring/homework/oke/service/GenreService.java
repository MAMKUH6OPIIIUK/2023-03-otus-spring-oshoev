package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.GenreDto;

import java.util.List;

public interface GenreService {
    List<GenreDto> findAll();
}
