package ru.otus.spring.homework.oke.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.spring.homework.oke.model.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {
}
