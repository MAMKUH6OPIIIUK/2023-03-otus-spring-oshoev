package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private long id;

    private String title;

    private String description;

    private AuthorResponseDto author;

    private Set<GenreResponseDto> genres;
}
