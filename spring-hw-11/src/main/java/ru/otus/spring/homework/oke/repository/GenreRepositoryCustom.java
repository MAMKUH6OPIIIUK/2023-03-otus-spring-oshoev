package ru.otus.spring.homework.oke.repository;

import reactor.core.publisher.Flux;
import ru.otus.spring.homework.oke.model.Genre;


public interface GenreRepositoryCustom {
    Flux<Genre> findAll();
}
