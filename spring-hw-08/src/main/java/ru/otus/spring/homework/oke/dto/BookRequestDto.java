package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private String id;

    private String title;

    private String description;

    private String authorId;

    private List<GenreDto> genres;

    public BookRequestDto(String title, String description, String authorId, List<GenreDto> genres) {
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.genres = genres;
    }
}
