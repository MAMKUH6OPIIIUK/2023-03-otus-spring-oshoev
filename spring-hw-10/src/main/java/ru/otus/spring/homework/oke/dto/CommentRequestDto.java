package ru.otus.spring.homework.oke.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.homework.oke.dto.validation.CreateCommentScope;
import ru.otus.spring.homework.oke.dto.validation.UpdateCommentScope;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    @Null(message = "{comment-id-field-should-not-be-present}", groups = CreateCommentScope.class)
    private Long id;

    @NotBlank(message = "{comment-text-field-should-not-be-blank}")
    @Size(min = 1, max = 1000, message = "{comment-text-field-should-has-expected-size}")
    private String text;

    @NotNull(message = "{comment-book-id-field-should-not-be-null}", groups = CreateCommentScope.class)
    @Null(message = "{comment-book-id-field-should-not-be-present}", groups = UpdateCommentScope.class)
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
