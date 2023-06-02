package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.List;

public interface BookService {
    BookResponseDto create(BookRequestDto bookRequestDto);

    void update(BookRequestDto bookRequestDto);

    BookResponseDto findById(String id);

    List<BookResponseDto> findAll();

    void deleteById(String id);
}
