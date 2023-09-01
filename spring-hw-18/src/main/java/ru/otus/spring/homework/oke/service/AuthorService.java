package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;

import java.util.List;

public interface AuthorService {
    AuthorFullNameDto create(AuthorRequestDto authorRequestDto);

    AuthorFullNameDto findById(Long id);

    List<AuthorFullNameDto> findAll();

    void deleteById(Long id);
}
