package ru.otus.spring.homework.oke.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class BookRequestDto {

    private Long id;

    @NotBlank(message="{book-title-field-should-not-be-blank}")
    @Size(min = 1, max=500, message = "{book-title-field-should-has-expected-size}")
    private String title;

    @NotBlank(message="{book-description-field-should-not-be-blank}")
    @Size(min = 1, max=1000, message = "{book-description-field-should-has-expected-size}")
    private String description;

    @Positive(message = "{book-author-field-should-be-positive}")
    private Long authorId;

    @NotEmpty(message = "{book-genres-field-should-not-be-empty}")
    private List<Long> genreIds;

    public BookRequestDto() {
        this.genreIds = Collections.EMPTY_LIST;
    }

    public BookRequestDto(String title, String description, Long authorId, List<Long> genreIds) {
        this.title = title;
        this.description = description;
        this.authorId = authorId;
        this.genreIds = genreIds;
    }
}
