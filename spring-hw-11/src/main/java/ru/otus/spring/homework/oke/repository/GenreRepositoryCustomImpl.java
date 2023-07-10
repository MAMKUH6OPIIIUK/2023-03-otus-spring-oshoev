package ru.otus.spring.homework.oke.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
    private final ReactiveMongoOperations mongoOperations;

    @Override
    public Flux<Genre> findAll() {
        Aggregation aggregation = newAggregation(
                unwind("genres")
                , group("genres.name")
                , project().andExclude("_id").and("_id").as("name")
        );
        return this.mongoOperations.aggregate(aggregation, Book.class, Genre.class);
    }
}
