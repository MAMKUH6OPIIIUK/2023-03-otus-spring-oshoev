package ru.otus.spring.homework.oke.dao;

import ru.otus.spring.homework.oke.domain.Author;

import java.util.List;

public interface AuthorsDao {

    Author create(Author genre);

    Author findById(long id);

    List<Author> findAll();

    void deleteById(long id);
}
