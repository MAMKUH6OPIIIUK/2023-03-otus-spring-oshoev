package ru.otus.spring.homework.oke.service;

import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;

import java.util.Date;
import java.util.List;

public interface CommentService {
    CommentResponseDto create(CommentRequestDto commentRequestDto);

    void update(CommentRequestDto commentRequestDto);

    CommentResponseDto findById(Long id);

    List<CommentResponseDto> findByBookId(Long bookId);

    void deleteById(Long id);

    long countByCreatedOnAfter(Date thresholdDate);
}
