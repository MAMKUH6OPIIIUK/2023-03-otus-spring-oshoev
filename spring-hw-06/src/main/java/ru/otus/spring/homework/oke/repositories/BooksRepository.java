package ru.otus.spring.homework.oke.repositories;

import ru.otus.spring.homework.oke.models.Book;

import java.util.List;
import java.util.Optional;

public interface BooksRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findAll();

    void deleteById(Long id);
}
