package ru.otus.spring.homework.oke.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.document.CommentDocument;
import ru.otus.spring.homework.oke.model.entity.Comment;

@Slf4j
@Component
public class CommentItemProcessListener implements ItemProcessListener<Comment, CommentDocument> {
    @Override
    public void afterProcess(@NonNull Comment comment, CommentDocument processedComment) {
        assert processedComment != null;
        log.debug(String.format("Обработан комментарий. Данные по идентификаторам помещены в кэш. Исходный " +
                "идентификатор: %s, сгенерированный идентификатор: %s", comment.getId(), processedComment.getId()));
    }
}
