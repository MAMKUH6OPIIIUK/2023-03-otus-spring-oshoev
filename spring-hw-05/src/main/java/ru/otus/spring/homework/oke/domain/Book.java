package ru.otus.spring.homework.oke.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public class Book {
    private long id;

    private String title;

    private String description;

    private long authorId;

    private Set<Long> genreIds;

    public Book(String title, String description, long authorId, Set<Long> genreIds) {
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
