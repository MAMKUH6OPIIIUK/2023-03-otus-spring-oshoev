package ru.otus.spring.homework.oke.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;

@Component
public class CommentMapper {
    public Comment mapToComment(CommentRequestDto requestDto, Book commentBook) {
        Comment comment = new Comment();
        comment.setId(requestDto.getId());
        comment.setText(requestDto.getText());
        comment.setBook(commentBook);
        return comment;
    }

    public Comment mergeCommentInfo(Comment comment, CommentRequestDto requestDto) {
        String newText = requestDto.getText();
        comment.setText(newText);
        return comment;
    }

    public CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto(comment.getId(), comment.getText());
        return dto;
    }

    public CommentRequestDto mapToCommentRequestDto(CommentResponseDto comment, Long bookId) {
        CommentRequestDto dto = new CommentRequestDto(comment.getId(), comment.getText(), bookId);
        return dto;
    }

}
