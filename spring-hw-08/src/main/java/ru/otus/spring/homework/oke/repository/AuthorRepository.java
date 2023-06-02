package ru.otus.spring.homework.oke.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.homework.oke.model.Author;

public interface AuthorRepository extends MongoRepository<Author, String> {
}
