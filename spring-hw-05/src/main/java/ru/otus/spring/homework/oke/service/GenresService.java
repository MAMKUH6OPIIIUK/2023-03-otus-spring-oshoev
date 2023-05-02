package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;

public interface GenresService {
    Genre create(String name);

    Genre findById(long id);

    List<Genre> findAll();

    void deleteById(long id);
}
