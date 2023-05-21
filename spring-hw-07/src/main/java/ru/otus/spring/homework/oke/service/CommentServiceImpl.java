package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        Book commentBook = this.validateBook(commentRequestDto.getBookId());
        Comment commentForCreate = this.commentMapper.mapToComment(commentRequestDto, commentBook);
        Comment createdComment = this.commentRepository.save(commentForCreate);
        return this.commentMapper.mapToCommentResponseDto(createdComment);
    }

    @Override
    @Transactional
    public void update(Long id, CommentRequestDto commentRequestDto) {
        this.validateComment(id);
        Book commentBook = this.validateBook(commentRequestDto.getBookId());
        Comment commentForUpdate = this.commentMapper.mapToComment(id, commentRequestDto, commentBook);
        this.commentRepository.save(commentForUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long id) {
        Comment foundComment = this.validateComment(id);
        CommentResponseDto result = this.commentMapper.mapToCommentResponseDto(foundComment);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByBookId(Long bookId) {
        List<Comment> foundComments = this.commentRepository.findByBookId(bookId);
        if (foundComments.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<CommentResponseDto> result = foundComments
                .stream()
                .map(this.commentMapper::mapToCommentResponseDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.commentRepository.deleteById(id);
    }

    private Comment validateComment(Long id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с указанным идентификатором " + id +
                        " не найден"));
        return comment;
    }

    private Book validateBook(Long bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Книга с указанным идентификатором " + bookId +
                        " не найдена"));
        return book;
    }
}
