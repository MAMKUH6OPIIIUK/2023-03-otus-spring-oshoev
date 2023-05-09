package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.List;

public interface BooksService {
    BookResponseDto create(BookRequestDto bookRequestDto);

    void update(long id, BookRequestDto bookRequestDto);

    BookResponseDto findById(long id);

    List<BookResponseDto> findAll();

    void deleteById(long id);
}
