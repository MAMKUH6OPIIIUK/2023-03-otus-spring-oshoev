package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.homework.oke.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
