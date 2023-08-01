package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.document.BookDocument;
import ru.otus.spring.homework.oke.model.document.CommentDocument;
import ru.otus.spring.homework.oke.model.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final BookMapper bookMapper;

    public CommentDocument mapToCommentDocumentWithGeneratedId(Comment comment) {
        CommentDocument mappedComment = new CommentDocument();
        String newCommentId = ObjectId.get().toHexString();
        mappedComment.setId(newCommentId);
        mappedComment.setText(comment.getText());
        BookDocument emptyBook = this.bookMapper.mapToEmptyBookDocumentWithCachedId(comment.getBookId());
        mappedComment.setBook(emptyBook);
        return mappedComment;
    }
}
