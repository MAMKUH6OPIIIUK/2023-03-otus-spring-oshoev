package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с жанрами должен")
@DataJpaTest
public class GenreRepositoryTest {
    private static final Long FIRST_GENRE_ID = 1L;

    private static final Long SECOND_GENRE_ID = 2L;

    private static final Long THIRD_GENRE_ID = 3L;

    private static final Long FIRST_NON_EXISTING_GENRE_ID = 4L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private GenreRepository genreRepository;

    @DisplayName("добавлять жанр в БД")
    @Test
    void shouldInsertGenre() {
        Genre genre = em.find(Genre.class, FIRST_NON_EXISTING_GENRE_ID);
        assertThat(genre).isNull();

        Genre genreForSave = new Genre("Очень интересный жанр");
        this.genreRepository.save(genreForSave);

        genre = em.find(Genre.class, FIRST_NON_EXISTING_GENRE_ID);
        assertThat(genre).usingRecursiveComparison().isEqualTo(genreForSave);
    }

    @DisplayName("возвращать ожидаемый жанр по его id")
    @Test
    void shouldReturnExpectedGenreById() {
        Genre expectedGenre = em.find(Genre.class, FIRST_GENRE_ID);
        Optional<Genre> actualOptionalGenre = this.genreRepository.findById(expectedGenre.getId());;
        assertThat(actualOptionalGenre).isPresent().get().usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("возвращать все существующие жанры при запросе всех жанров")
    @Test
    void shouldReturnExpectedGenresList() {
        Genre expectedGenre1 = em.find(Genre.class, FIRST_GENRE_ID);
        Genre expectedGenre2 = em.find(Genre.class, SECOND_GENRE_ID);
        Genre expectedGenre3 = em.find(Genre.class, THIRD_GENRE_ID);
        List<Genre> actualGenres = this.genreRepository.findAll();
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedGenre1, expectedGenre2, expectedGenre3);
    }

    @DisplayName("возвращать ожидаемый набор жанров по набору их идентификаторов")
    @Test
    void shouldReturnExpectedGenresByIds() {
        Genre expectedGenre1 = em.find(Genre.class, FIRST_GENRE_ID);
        Genre expectedGenre2 = em.find(Genre.class, SECOND_GENRE_ID);
        Set<Long> ids = Set.of(FIRST_GENRE_ID, SECOND_GENRE_ID);
        Set<Genre> actualGenres = this.genreRepository.findByIdIn(ids);
        assertThat(actualGenres)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedGenre1, expectedGenre2);

    }

    @DisplayName("удалять заданный жанр по его id")
    @Test
    void shouldCorrectDeleteGenreById() {
        Genre genre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(genre).isNotNull();

        this.genreRepository.deleteById(FIRST_GENRE_ID);

        genre = em.find(Genre.class, FIRST_GENRE_ID);
        assertThat(genre).isNull();
    }
}
