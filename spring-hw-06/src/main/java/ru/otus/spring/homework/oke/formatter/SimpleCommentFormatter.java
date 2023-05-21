package ru.otus.spring.homework.oke.formatter;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.formatter.utils.IndentUtils;

import java.util.List;

@Component
public class SimpleCommentFormatter implements CommentFormatter {
    @Override
    public String formatComment(CommentResponseDto comment, int indent) {
        String indentPrefix = IndentUtils.getEntityPrefix(indent);
        String linePrefix = IndentUtils.getEntityLinePrefix(indent);
        StringBuilder builder = new StringBuilder();
        builder.append(indentPrefix + "Комментарий:");
        builder.append(linePrefix);
        builder.append("Идентификатор: ");
        builder.append(comment.getId());
        builder.append(linePrefix);
        builder.append("Текст: ");
        builder.append(comment.getText());
        return builder.toString();
    }

    @Override
    public String formatComments(List<CommentResponseDto> comments, int indent) {
        StringBuilder builder = new StringBuilder();
        comments.forEach(c -> {
            builder.append(this.formatComment(c, indent) + System.lineSeparator());
        });
        return builder.toString();
    }
}
