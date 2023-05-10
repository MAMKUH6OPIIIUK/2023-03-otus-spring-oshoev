package ru.otus.spring.homework.oke.dao;

import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;
import java.util.Set;

public interface GenresDao {

    Genre create(Genre genre);

    Genre findById(long id);

    List<Genre> findAll();

    List<Genre> findAllUsed();

    List<Genre> findAllByBookId(long bookId);

    List<Genre> findByIds(Set<Long> ids);

    void deleteById(long id);
}
