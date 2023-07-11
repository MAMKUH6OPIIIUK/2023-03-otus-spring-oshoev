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
        comment.setBookId(commentBook.getId());
        return comment;
    }

    public Comment mergeCommentInfo(Comment comment, CommentRequestDto requestDto) {
        Comment mergedComment = new Comment();
        mergedComment.setId(comment.getId());
        mergedComment.setText(requestDto.getText());
        mergedComment.setBookId(comment.getBookId());
        return mergedComment;
    }

    public CommentResponseDto mapToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto(comment.getId(), comment.getText());
        return dto;
    }

    public CommentRequestDto mapToCommentRequestDto(CommentResponseDto comment, String bookId) {
        CommentRequestDto dto = new CommentRequestDto(comment.getId(), comment.getText(), bookId);
        return dto;
    }

}
