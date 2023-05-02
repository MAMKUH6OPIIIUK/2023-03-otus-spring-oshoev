package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.domain.Author;

import java.util.List;

public interface AuthorFormatter {
    String formatAuthor(Author author, int indent);

    String formatAuthors(List<Author> authors, int indent);
}
