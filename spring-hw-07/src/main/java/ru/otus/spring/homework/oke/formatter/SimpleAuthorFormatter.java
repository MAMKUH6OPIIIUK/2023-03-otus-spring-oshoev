package ru.otus.spring.homework.oke.formatter;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.formatter.utils.IndentUtils;

import java.util.List;

@Component
public class SimpleAuthorFormatter implements AuthorFormatter {
    @Override
    public String formatAuthor(AuthorResponseDto author, int indent) {
        String indentPrefix = IndentUtils.getEntityPrefix(indent);
        String linePrefix = IndentUtils.getEntityLinePrefix(indent);
        StringBuilder builder = new StringBuilder();
        builder.append(indentPrefix + "Автор:");
        builder.append(linePrefix);
        builder.append("Идентификатор: ");
        builder.append(author.getId());
        builder.append(linePrefix);
        builder.append("Имя: ");
        builder.append(author.getName());
        builder.append(linePrefix);
        builder.append("Среднее имя: ");
        builder.append(author.getMiddleName());
        builder.append(linePrefix);
        builder.append("Отчество: ");
        builder.append(author.getPatronymic());
        builder.append(linePrefix);
        builder.append("Фамилия: ");
        builder.append(author.getSurname());
        return builder.toString();
    }

    @Override
    public String formatAuthors(List<AuthorResponseDto> authors, int indent) {
        StringBuilder builder = new StringBuilder();
        authors.forEach(a -> {
            builder.append(this.formatAuthor(a, indent) + System.lineSeparator());
        });
        return builder.toString();
    }
}
