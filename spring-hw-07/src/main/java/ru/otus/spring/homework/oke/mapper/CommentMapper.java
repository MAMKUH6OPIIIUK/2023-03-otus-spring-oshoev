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
        comment.setText(requestDto.getText());
        comment.setBook(commentBook);
        return comment;
    }

    public Comment mapToComment(Long id, CommentRequestDto requestDto, Book commentBook) {
        Comment comment = mapToComment(requestDto, commentBook);
        comment.setId(id);
        return comment;
    }

    public CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto(comment.getId(), comment.getText());
        return dto;
    }

}
