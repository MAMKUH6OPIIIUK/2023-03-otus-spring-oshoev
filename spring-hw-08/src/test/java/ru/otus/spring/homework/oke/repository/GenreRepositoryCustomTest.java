package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами должен")
@DataMongoTest
@Import(GenreRepositoryCustomImpl.class)
public class GenreRepositoryCustomTest {
    @Autowired
    private GenreRepositoryCustom genreRepository;

    @DisplayName("возвращать все существующие жанры при запросе всех жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        Genre expectedGenre1 = new Genre("Жанр1");
        Genre expectedGenre2 = new Genre("Жанр2");
        Genre expectedGenre3 = new Genre("Жанр3");
        List<Genre> actualGenres = this.genreRepository.findAll();
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedGenre1, expectedGenre2, expectedGenre3);
    }
}
