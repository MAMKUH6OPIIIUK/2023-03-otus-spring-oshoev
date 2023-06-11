package ru.otus.spring.homework.oke.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;

    @NotBlank(message="{comment-text-field-should-not-be-blank}")
    @Size(min = 1, max=1000, message = "{comment-text-field-should-has-expected-size}")
    private String text;

    @Positive(message = "comment-book-field-should-be-positive")
    private Long bookId;

    public CommentRequestDto(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public CommentRequestDto(String text, Long bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
