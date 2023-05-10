package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.domain.Book;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Дао для работы с книгами должно ")
@JdbcTest
@Import({JdbcGenresDao.class, JdbcBooksDao.class})
public class JdbcBooksDaoTest {
    private static final long FIRST_NON_EXISTING_BOOK_ID = 4;

    private static final long EXISTING_BOOK_ID = 3;

    @Autowired
    BooksDao booksDao;

    @DisplayName("добавлять книгу с привязкой к автору и жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        Book expectedBook = new Book(FIRST_NON_EXISTING_BOOK_ID, "Книга4", "Описание4", 1,
                Set.of(1L));
        this.booksDao.create(expectedBook);
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("бросить исключение DataIntegrityViolationException при попытке создать книгу с несуществующим автором")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenInsertWithNonExistingAuthor() {
        long notExistingAuthorId = 4;
        Book book = new Book("Любое", "Любое", notExistingAuthorId, new HashSet<>());
        assertThatThrownBy(() -> this.booksDao.create(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("бросить исключение DataIntegrityViolationException при попытке создать книгу с несуществующим жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenInsertWithNonExistingGenre() {
        long notExistingGenreId = 4;
        Book book = new Book("Любое", "Любое", 1, Set.of(notExistingGenreId));
        assertThatThrownBy(() -> this.booksDao.create(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("обновлять книгу и её привязки к жанрам в БД")
    @Test
    void shouldUpdateBook() {
        Book expectedBook = new Book(EXISTING_BOOK_ID, "Другое название", "Другое описание", 1,
                Set.of(3L));
        this.booksDao.update(expectedBook);
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("бросить исключение IncorrectResultSizeDataAccessException при попытке обновить несуществующую книгу")
    @Test
    void shouldThrowIncorrectResultSizeDataAccessExceptionWhenUpdateNonExistingBook() {
        Book book = new Book(FIRST_NON_EXISTING_BOOK_ID, "Любое", "Любое", 4, Set.of(3L));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @DisplayName("бросить исключение DataIntegrityViolationException при попытке обновить книгу с указанием несуществующего " +
            "автора")
    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenUpdateWithNonExistingAuthor() {
        long notExistingAuthorId = 4;
        Book book = new Book(EXISTING_BOOK_ID, "Любое", "Любое",
                notExistingAuthorId, Set.of(4L));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("бросить исключение DataIntegrityViolationException при попытке обновить книгу с указанием " +
            "несуществующего жанра")
    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenUpdateWithNonExistingGenre() {
        long notExistingGenreId = 4;
        Book book = new Book(EXISTING_BOOK_ID, "Любое", "Любое",
                1, Set.of(notExistingGenreId));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        Book expectedBook = new Book(EXISTING_BOOK_ID, "Книга3", "Описание3", 2,
                Set.of(2L));
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг по идентификатору автора")
    @Test
    void shouldReturnExpectedBookListByAuthorId() {
        long authorId = 2;
        Book expectedBook = new Book(3, "Книга3", "Описание3", authorId,
                Set.of(2L));
        List<Book> actualBooks = this.booksDao.findByAuthorId(authorId);
        assertThat(actualBooks).containsExactlyInAnyOrder(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBookList() {
        Book expectedBook1 = new Book(1, "Книга1", "Описание1", 1,
                Set.of(1L, 2L));
        Book expectedBook2 = new Book(2, "Книга2", "Описание2", 1,
                Set.of(1L, 2L));
        Book expectedBook3 = new Book(EXISTING_BOOK_ID, "Книга3", "Описание3", 2,
                Set.of(2L));
        List<Book> actualBooks = this.booksDao.findAll();
        assertThat(actualBooks).containsExactlyInAnyOrder(expectedBook1, expectedBook2, expectedBook3);
    }

    @DisplayName("удалять книгу по её идентификатору")
    @Test
    void shouldCorrectDeleteBookById() {
        int expectedBookCount = 2;
        assertThatCode(() -> this.booksDao.findById(EXISTING_BOOK_ID)).doesNotThrowAnyException();
        this.booksDao.deleteById(EXISTING_BOOK_ID);
        int actualBookCount = this.booksDao.findAll().size();
        assertThatThrownBy(() -> this.booksDao.findById(EXISTING_BOOK_ID))
                .isInstanceOf(EmptyResultDataAccessException.class);
        assertThat(actualBookCount).isEqualTo(expectedBookCount);

    }
}
