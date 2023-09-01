package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.homework.oke.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    long countByAuthorId(Long authorId);

    @Override
    @EntityGraph(value = "book-author-entity-graph")
    Optional<Book> findById(Long id);

    @Override
    @EntityGraph(value = "book-author-entity-graph")
    List<Book> findAll();

}
