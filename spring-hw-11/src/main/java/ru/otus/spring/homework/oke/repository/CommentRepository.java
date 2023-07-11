package ru.otus.spring.homework.oke.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.homework.oke.model.Comment;


public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {
    Flux<Comment> findByBookId(String bookId);

    @DeleteQuery
    Mono<Void> deleteByBookId(String bookId);
}
