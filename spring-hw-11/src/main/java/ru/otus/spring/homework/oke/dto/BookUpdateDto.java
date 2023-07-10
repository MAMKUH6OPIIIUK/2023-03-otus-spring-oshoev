package ru.otus.spring.homework.oke.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.spring.homework.oke.dto.validation.UpdateBookFullScope;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class BookUpdateDto {
    private String id;

    @NotBlank(message = "{book-title-field-should-not-be-blank}", groups = UpdateBookFullScope.class)
    @Size(min = 1, max = 500, message = "{book-title-field-should-has-expected-size}",
            groups = UpdateBookFullScope.class)
    private String title;

    @NotBlank(message = "{book-description-field-should-not-be-blank}", groups = UpdateBookFullScope.class)
    @Size(min = 1, max = 1000, message = "{book-description-field-should-has-expected-size}",
            groups = UpdateBookFullScope.class)
    private String description;

    @NotNull(message = "{book-author-field-should-not-be-null}", groups = UpdateBookFullScope.class)
    private String authorId;

    @NotEmpty(message = "{book-genres-field-should-not-be-empty}", groups = UpdateBookFullScope.class)
    private List<GenreDto> genres;

    public BookUpdateDto() {
        this.genres = Collections.EMPTY_LIST;
    }
}
