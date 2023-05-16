package ru.otus.spring.homework.oke.repositories;

import ru.otus.spring.homework.oke.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository {
    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    List<Comment> findByBookId(Long bookId);

    void deleteById(Long id);
}
