package ru.otus.spring.homework.oke.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.spring.homework.oke.model.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByBookId(String bookId);

    @DeleteQuery
    void deleteByBookId(String bookId);
}
