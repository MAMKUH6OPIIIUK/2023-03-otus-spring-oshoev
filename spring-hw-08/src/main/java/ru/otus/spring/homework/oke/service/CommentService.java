package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto create(CommentRequestDto commentRequestDto);

    void update(CommentRequestDto commentRequestDto);

    CommentResponseDto findById(String id);

    List<CommentResponseDto> findByBookId(String bookId);

    void deleteById(String id);
}
