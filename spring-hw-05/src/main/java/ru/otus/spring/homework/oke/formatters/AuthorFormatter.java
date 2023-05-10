package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorFormatter {
    String formatAuthor(AuthorResponseDto author, int indent);

    String formatAuthors(List<AuthorResponseDto> authors, int indent);
}
