package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private Long id;

    private String text;

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
