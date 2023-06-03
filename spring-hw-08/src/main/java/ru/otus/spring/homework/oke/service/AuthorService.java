package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {
    AuthorResponseDto create(AuthorRequestDto authorRequestDto);

    AuthorResponseDto findById(String id);

    List<AuthorResponseDto> findAll();

    void deleteById(String id);
}
