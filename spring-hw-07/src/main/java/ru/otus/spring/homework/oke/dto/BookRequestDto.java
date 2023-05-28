package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private Long id;

    private String title;

    private String description;

    private Long authorId;

    private Set<Long> genreIds;

    public BookRequestDto(String title, String description, Long authorId, Set<Long> genreIds) {
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
