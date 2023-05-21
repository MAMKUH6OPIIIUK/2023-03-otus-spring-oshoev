package ru.otus.spring.homework.oke.repository;

import ru.otus.spring.homework.oke.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author genre);

    Optional<Author> findById(Long id);

    List<Author> findAll();

    void deleteById(Long id);
}
