package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.spring.homework.oke.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Дао для работы с жанрами должно ")
@JdbcTest
@Import(GenresDaoJdbc.class)
public class GenresDaoJdbcTest {
    private static final long EXISTING_GENRE_ID = 1;

    private static final String EXISTING_GENRE_NAME = "Жанр1";

    private static final long FIRST_NON_EXISTING_GENRE_ID = 4;

    private static final long EXISTING_BOOK_ID = 3;

    @Autowired
    GenresDao genresDao;

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        Genre expectedGenre = new Genre(FIRST_NON_EXISTING_GENRE_ID, "Очень интересный жанр");
        this.genresDao.create(expectedGenre);
        Genre actualGenre = this.genresDao.findById(expectedGenre.getId());
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        Genre actualGenre = this.genresDao.findById(expectedGenre.getId());
        assertThat(actualGenre).isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать все существующие жанры при запросе всех жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        Genre expectedGenre1 = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        Genre expectedGenre2 = new Genre(2, "Жанр2");
        Genre expectedGenre3 = new Genre(3, "Жанр3");
        List<Genre> actualGenres = this.genresDao.findAll();
        assertThat(actualGenres).containsExactlyInAnyOrder(expectedGenre1, expectedGenre2, expectedGenre3);
    }

    @DisplayName("возвращать только связанные с книгами жанры при запросе используемых жанров")
    @Test
    void shouldReturnExpectedUsedGenresList() {
        Genre expectedGenre1 = new Genre(EXISTING_GENRE_ID, EXISTING_GENRE_NAME);
        Genre expectedGenre2 = new Genre(2, "Жанр2");
        List<Genre> actualUsedGenres = this.genresDao.findAllUsed();
        assertThat(actualUsedGenres).containsExactlyInAnyOrder(expectedGenre1, expectedGenre2);
    }

    @DisplayName("возвращать ожидаемый список жанров книги по её id")
    @Test
    void shouldReturnExpectedBookGenresByBookId() {
        Genre expectedGenre = new Genre(2, "Жанр2");
        List<Genre> actualBookGenres = this.genresDao.findAllByBookId(EXISTING_BOOK_ID);
        assertThat(actualBookGenres).containsExactlyInAnyOrder(expectedGenre);
    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {
        assertThatCode(() -> this.genresDao.findById(EXISTING_GENRE_ID)).doesNotThrowAnyException();

        this.genresDao.deleteById(EXISTING_GENRE_ID);

        assertThatThrownBy(() -> this.genresDao.findById(EXISTING_GENRE_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
