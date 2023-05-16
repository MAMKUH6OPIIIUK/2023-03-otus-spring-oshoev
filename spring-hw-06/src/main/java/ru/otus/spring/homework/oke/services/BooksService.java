package ru.otus.spring.homework.oke.services;

import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.List;

public interface BooksService {
    BookResponseDto create(BookRequestDto bookRequestDto);

    void update(Long id, BookRequestDto bookRequestDto);

    BookResponseDto findById(Long id);

    List<BookResponseDto> findAll();

    void deleteById(Long id);
}
