package ru.otus.spring.homework.oke.repository;

import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreRepository {
    Genre save(Genre genre);

    Optional<Genre> findById(Long id);

    List<Genre> findByIds(Set<Long> ids);

    List<Genre> findAll();

    void deleteById(Long id);
}
