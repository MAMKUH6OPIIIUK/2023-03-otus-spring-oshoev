package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.List;

public interface BookFormatter {
    String formatBook(BookResponseDto book, int indent);

    String formatBooks(List<BookResponseDto> books, int indent);
}

