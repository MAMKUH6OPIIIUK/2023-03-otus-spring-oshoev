package ru.otus.spring.homework.oke.formatters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.formatters.utils.IndentUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SimpleBookFormatter implements BookFormatter {
    private final AuthorFormatter authorFormatter;

    private final GenreFormatter genreFormatter;

    @Override
    public String formatBook(BookResponseDto book, int indent) {
        String indentPrefix = IndentUtils.getEntityPrefix(indent);
        String linePrefix = IndentUtils.getEntityLinePrefix(indent);
        StringBuilder builder = new StringBuilder();
        builder.append(indentPrefix + "Книга: ");
        builder.append(linePrefix);
        builder.append("Идентификатор: ");
        builder.append(book.getId());
        builder.append(linePrefix);
        builder.append("Наименование: ");
        builder.append(book.getTitle());
        builder.append(linePrefix);
        builder.append("Аннотация: ");
        builder.append(book.getDescription());
        builder.append(System.lineSeparator());
        builder.append(this.authorFormatter.formatAuthor(book.getAuthor(),indent + 1));
        builder.append(System.lineSeparator());
        builder.append(this.genreFormatter.formatGenres(book.getGenres().stream().toList(), indent + 1));
        return builder.toString();
    }

    @Override
    public String formatBooks(List<BookResponseDto> books, int indent) {
        StringBuilder builder = new StringBuilder();
        books.forEach(b -> {
            builder.append(this.formatBook(b, indent) + System.lineSeparator());
        });
        return builder.toString();
    }
}
