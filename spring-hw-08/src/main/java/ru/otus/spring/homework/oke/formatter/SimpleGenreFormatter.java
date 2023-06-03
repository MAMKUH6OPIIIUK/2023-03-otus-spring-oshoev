package ru.otus.spring.homework.oke.formatter;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.GenreDto;
import ru.otus.spring.homework.oke.formatter.utils.IndentUtils;

import java.util.List;

@Component
public class SimpleGenreFormatter implements GenreFormatter {

    @Override
    public String formatGenre(GenreDto genre, int indent) {
        String indentPrefix = IndentUtils.getEntityPrefix(indent);
        String linePrefix = IndentUtils.getEntityLinePrefix(indent);
        StringBuilder builder = new StringBuilder();
        builder.append(indentPrefix + "Жанр:");
        builder.append(linePrefix);
        builder.append("Наименование: ");
        builder.append(genre.getName());
        return builder.toString();
    }

    @Override
    public String formatGenres(List<GenreDto> genres, int indent) {
        StringBuilder builder = new StringBuilder();
        genres.forEach(g -> {
            builder.append(this.formatGenre(g, indent) + System.lineSeparator());
        });
        return builder.toString();
    }
}
