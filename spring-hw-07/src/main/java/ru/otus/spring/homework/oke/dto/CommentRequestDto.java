package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;

    private String text;

    private Long bookId;

    public CommentRequestDto(String text, Long bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
