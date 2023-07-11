package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDto {
    private String id;

    private String title;

    private String description;

    private AuthorFullNameDto author;

    private List<GenreDto> genres;
}
