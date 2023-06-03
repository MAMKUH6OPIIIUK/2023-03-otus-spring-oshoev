package ru.otus.spring.homework.oke.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequestDto {
    private String id;

    private String text;

    private String bookId;

    public CommentRequestDto(String text, String bookId) {
        this.text = text;
        this.bookId = bookId;
    }
}
