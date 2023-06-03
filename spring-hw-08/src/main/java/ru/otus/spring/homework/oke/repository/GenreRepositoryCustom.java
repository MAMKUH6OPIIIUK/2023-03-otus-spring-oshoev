package ru.otus.spring.homework.oke.repository;

import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

public interface GenreRepositoryCustom {
    List<Genre> findAll();
}
