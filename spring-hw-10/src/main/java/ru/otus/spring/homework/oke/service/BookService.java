package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookResponseDto create(BookCreateDto bookCreateDto);

    void update(BookUpdateDto bookUpdateDto);

    BookResponseDto findById(Long id);

    List<BookResponseDto> findAll();

    void deleteById(Long id);
}
