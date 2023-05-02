package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.domain.Book;

import java.util.List;

public interface BookFormatter {
    String formatBook(Book book, int indent);

    String formatBooks(List<Book> books, int indent);
}
