package ru.otus.spring.homework.oke.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.model.document.CommentDocument;
import ru.otus.spring.homework.oke.model.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentItemProcessor implements ItemProcessor<Comment, CommentDocument> {
    private final CommentMapper commentMapper;

    @Override
    public CommentDocument process(@NonNull Comment comment) {
        return this.commentMapper.mapToCommentDocumentWithGeneratedId(comment);
    }
}
