package ru.otus.spring.homework.oke.dao;

import ru.otus.spring.homework.oke.domain.Book;

import java.util.List;

public interface BooksDao {

    Book create(Book book);

    void update(Book book);

    Book findById(long id);

    List<Book> findByAuthorId(long authorId);

    List<Book> findAll();

    void deleteById(long id);
}
