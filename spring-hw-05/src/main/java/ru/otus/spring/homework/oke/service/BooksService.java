package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.domain.Book;

import java.util.List;

public interface BooksService {
    Book create(String title, String description, long authorId, long[] genreIds);

    Book update(long id, String title, String description, long authorId, long[] genreIds);

    Book findById(long id);

    List<Book> findAll();

    void deleteById(long id);
}
