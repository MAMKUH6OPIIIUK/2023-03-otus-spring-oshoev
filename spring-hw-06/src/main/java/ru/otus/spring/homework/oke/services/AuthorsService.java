package ru.otus.spring.homework.oke.services;

import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorsService {
    AuthorResponseDto create(AuthorRequestDto authorRequestDto);

    AuthorResponseDto findById(Long id);

    List<AuthorResponseDto> findAll();

    void deleteById(Long id);
}
