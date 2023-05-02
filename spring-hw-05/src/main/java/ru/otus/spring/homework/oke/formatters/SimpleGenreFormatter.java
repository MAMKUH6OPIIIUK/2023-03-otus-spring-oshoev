package ru.otus.spring.homework.oke.formatters;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.formatters.utils.IndentUtils;

import java.util.List;

@Component
public class SimpleGenreFormatter implements GenreFormatter {

    @Override
    public String formatGenre(Genre genre, int indent) {
        String indentPrefix = IndentUtils.getEntityPrefix(indent);
        String linePrefix = IndentUtils.getEntityLinePrefix(indent);
        StringBuilder builder = new StringBuilder();
        builder.append(indentPrefix + "Жанр:");
        builder.append(linePrefix);
        builder.append("Идентификатор: ");
        builder.append(genre.getId());
        builder.append(linePrefix);
        builder.append("Наименование: ");
        builder.append(genre.getName());
        return builder.toString();
    }

    @Override
    public String formatGenres(List<Genre> genres, int indent) {
        StringBuilder builder = new StringBuilder();
        genres.forEach(g -> {
            builder.append(this.formatGenre(g, indent) + System.lineSeparator());
        });
        return builder.toString();
    }
}
