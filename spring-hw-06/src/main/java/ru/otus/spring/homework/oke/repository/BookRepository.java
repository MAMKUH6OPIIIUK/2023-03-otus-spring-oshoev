package ru.otus.spring.homework.oke.repository;

import ru.otus.spring.homework.oke.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(Long id);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findAll();

    void deleteById(Long id);
}
