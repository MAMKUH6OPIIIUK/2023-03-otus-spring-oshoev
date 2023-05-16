package ru.otus.spring.homework.oke.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.mappers.CommentMapper;
import ru.otus.spring.homework.oke.models.Book;
import ru.otus.spring.homework.oke.models.Comment;
import ru.otus.spring.homework.oke.repositories.BooksRepository;
import ru.otus.spring.homework.oke.repositories.CommentsRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;

    private final BooksRepository booksRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        Book commentBook = this.validateBook(commentRequestDto.getBookId());
        Comment commentForCreate = this.commentMapper.mapToComment(commentRequestDto, commentBook);
        Comment createdComment = this.commentsRepository.save(commentForCreate);
        return this.commentMapper.mapToCommentResponseDto(createdComment);
    }

    @Override
    @Transactional
    public void update(Long id, CommentRequestDto commentRequestDto) {
        this.validateComment(id);
        Book commentBook = this.validateBook(commentRequestDto.getBookId());
        Comment commentForUpdate = this.commentMapper.mapToComment(id, commentRequestDto, commentBook);
        this.commentsRepository.save(commentForUpdate);
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
        List<Comment> foundComments = this.commentsRepository.findByBookId(bookId);
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
        this.commentsRepository.deleteById(id);
    }

    private Comment validateComment(Long id) {
        Comment comment = this.commentsRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new NotFoundException("Комментарий с указанным идентификатором " + id + " не найден");
        }
        return comment;
    }

    private Book validateBook(Long bookId) {
        Book book = this.booksRepository.findById(bookId).orElse(null);
        if (book == null) {
            throw new NotFoundException("Книга с указанным идентификатором " + bookId + " не найдена");
        }
        return book;
    }
}
