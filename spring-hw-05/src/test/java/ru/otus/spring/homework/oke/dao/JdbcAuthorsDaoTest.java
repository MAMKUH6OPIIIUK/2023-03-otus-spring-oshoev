package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.spring.homework.oke.domain.Author;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Дао для работы с авторами должно ")
@JdbcTest
@Import(JdbcAuthorsDao.class)
public class JdbcAuthorsDaoTest {
    private static final long EXISTING_AUTHOR_ID = 3;

    private static final long FIRST_NON_EXISTING_AUTHOR_ID = 4;

    @Autowired
    AuthorsDao authorsDao;

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author expectedAuthor = new Author(FIRST_NON_EXISTING_AUTHOR_ID, "Иван", null, "Иванович", "Иванов");
        this.authorsDao.create(expectedAuthor);
        Author actualAuthor = this.authorsDao.findById(FIRST_NON_EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(EXISTING_AUTHOR_ID, "Имя3", null, "Отчество3", "Фамилия3");
        Author actualAuthor = this.authorsDao.findById(EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorList() {
        Author expectedAuthor1 = new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Author expectedAuthor2 = new Author(2, "Имя2", null, "Отчество2", "Фамилия2");
        Author expectedAuthor3 = new Author(EXISTING_AUTHOR_ID, "Имя3", null, "Отчество3", "Фамилия3");
        List<Author> actualAuthors = this.authorsDao.findAll();
        assertThat(actualAuthors).containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2, expectedAuthor3);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        int expectedAuthorCount = 2;
        assertThatCode(() -> this.authorsDao.findById(EXISTING_AUTHOR_ID)).doesNotThrowAnyException();
        this.authorsDao.deleteById(EXISTING_AUTHOR_ID);
        int actualAuthorCount = this.authorsDao.findAll().size();
        assertThatThrownBy(() -> this.authorsDao.findById(EXISTING_AUTHOR_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(actualAuthorCount).isEqualTo(expectedAuthorCount);
    }

    @DisplayName("бросить исключение DataIntegrityViolationException при попытке удалить автора, у которого есть книги")
    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenDeleteWithExistingBooks() {
        assertThatThrownBy(() -> this.authorsDao.deleteById(1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
