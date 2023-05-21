package ru.otus.spring.homework.oke.repository;

import ru.otus.spring.homework.oke.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findByBookId(Long bookId);

    void deleteById(Long id);
}
