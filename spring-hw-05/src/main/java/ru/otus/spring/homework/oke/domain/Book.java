package ru.otus.spring.homework.oke.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Data
public class Book {
    private final long id;

    private final String title;

    private final String description;

    private final Author author;

    private final Set<Genre> genres;
}
