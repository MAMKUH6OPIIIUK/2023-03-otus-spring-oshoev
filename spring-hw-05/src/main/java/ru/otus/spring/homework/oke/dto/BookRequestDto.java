package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private String title;

    private String description;

    private Long authorId;

    private Set<Long> genreIds;
}
