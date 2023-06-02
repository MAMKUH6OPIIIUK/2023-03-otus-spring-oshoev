package ru.otus.spring.homework.oke.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryCustomImpl implements GenreRepositoryCustom {
    private final MongoOperations mongoOperations;

    @Override
    public List<Genre> findAll() {
        Aggregation aggregation = newAggregation(
                unwind("genres")
                , group("genres.name")
                , project().andExclude("_id").and("_id").as("name")
        );
        return this.mongoOperations.aggregate(aggregation, Book.class, Genre.class).getMappedResults();
    }
}
