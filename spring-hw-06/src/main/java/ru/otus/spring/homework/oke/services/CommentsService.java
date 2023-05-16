package ru.otus.spring.homework.oke.services;

import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;

import java.util.List;

public interface CommentsService {
    CommentResponseDto create(CommentRequestDto commentRequestDto);

    void update(Long id, CommentRequestDto commentRequestDto);

    CommentResponseDto findById(Long id);

    List<CommentResponseDto> findByBookId(Long bookId);

    void deleteById(Long id);
}
