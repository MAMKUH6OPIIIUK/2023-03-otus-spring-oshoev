package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.homework.oke.model.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Override
    List<Author> findAll();
}
