package ru.otus.spring.homework.oke.formatters;

import ru.otus.spring.homework.oke.dto.CommentResponseDto;

import java.util.List;

public interface CommentFormatter {
    String formatComment(CommentResponseDto comment, int indent);

    String formatComments(List<CommentResponseDto> comments, int indent);
}
