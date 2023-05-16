package ru.otus.spring.homework.oke.repositories;

import ru.otus.spring.homework.oke.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsRepository {
    Author save(Author genre);

    Optional<Author> findById(Long id);

    List<Author> findAll();

    void deleteById(Long id);
}
