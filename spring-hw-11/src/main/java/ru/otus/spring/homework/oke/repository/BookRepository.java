package ru.otus.spring.homework.oke.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.homework.oke.model.Book;

import java.util.List;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
    List<Book> findByAuthorId(String authorId);

    boolean existsByAuthorId(String authorId);
}
