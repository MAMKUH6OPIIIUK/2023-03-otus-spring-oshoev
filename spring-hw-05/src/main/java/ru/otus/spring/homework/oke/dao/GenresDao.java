package ru.otus.spring.homework.oke.dao;

import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;

public interface GenresDao {

    Genre create(Genre genre);

    Genre findById(long id);

    List<Genre> findAll();

    List<Genre> findAllUsed();

    List<Genre> findAllByBookId(long bookId);

    void deleteById(long id);
}
