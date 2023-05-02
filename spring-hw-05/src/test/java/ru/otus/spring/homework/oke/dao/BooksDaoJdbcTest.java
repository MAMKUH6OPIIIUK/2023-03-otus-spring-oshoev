package ru.otus.spring.homework.oke.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.exceptions.AuthorNotFoundException;
import ru.otus.spring.homework.oke.exceptions.BookNotFoundException;
import ru.otus.spring.homework.oke.exceptions.GenreNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Дао для работы с книгами должно ")
@JdbcTest
@Import({GenresDaoJdbc.class, BooksDaoJdbc.class})
public class BooksDaoJdbcTest {
    private static final long FIRST_NON_EXISTING_BOOK_ID = 4;

    private static final long EXISTING_BOOK_ID = 3;

    @Autowired
    BooksDao booksDao;

    @DisplayName("добавлять книгу с привязкой к автору и жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        Author expectedAuthor = new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Genre expectedGenre = new Genre(1, "Жанр1");
        Book expectedBook = new Book(FIRST_NON_EXISTING_BOOK_ID, "Книга4", "Описание4", expectedAuthor,
                Set.of(expectedGenre));
        this.booksDao.create(expectedBook);
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("бросить исключение AuthorNotFoundException при попытке создать книгу с несуществующим автором")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldThrowAuthorNotFoundExceptionWhenInsertWithNonExistingAuthor() {
        Author notExistingAuthor = new Author(4, "Не автор", "Не автор", null, "Не автор");
        Book book = new Book(0, "Любое", "Любое", notExistingAuthor, new HashSet<>());
        assertThatThrownBy(() -> this.booksDao.create(book)).isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("бросить исключение GenreNotFoundException при попытке создать книгу с несуществующим жанром")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldThrowGenreNotFoundExceptionWhenInsertWithNonExistingGenre() {
        Author existingAuthor = new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Genre notExistingGenre = new Genre(4, "Не жанр");
        Book book = new Book(0, "Любое", "Любое", existingAuthor, Set.of(notExistingGenre));
        assertThatThrownBy(() -> this.booksDao.create(book)).isInstanceOf(GenreNotFoundException.class);
    }

    @DisplayName("обновлять книгу и её привязки к жанрам в БД")
    @Test
    void shouldUpdateBook() {
        Author anotherAuthor = new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Genre anotherGenre = new Genre(3, "Жанр3");
        Book expectedBook = new Book(EXISTING_BOOK_ID, "Другое название", "Другое описание", anotherAuthor,
                Set.of(anotherGenre));
        this.booksDao.update(expectedBook);
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("бросить исключение BookNotFoundException при попытке обновить несуществующую книгу")
    @Test
    void shouldThrowAuthorNotFoundExceptionWhenUpdateNonExistingBook() {
        Author author = new Author(4, "Не автор", "Не автор", null, "Не автор");
        Genre genre = new Genre(3, "Жанр3");
        Book book = new Book(FIRST_NON_EXISTING_BOOK_ID, "Любое", "Любое", author, Set.of(genre));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(BookNotFoundException.class);
    }

    @DisplayName("бросить исключение AuthorNotFoundException при попытке обновить книгу с указанием несуществующего " +
            "автора")
    @Test
    void shouldThrowAuthorNotFoundExceptionWhenUpdateWithNonExistingAuthor() {
        Author notExistingAuthor = new Author(4, "Не автор", "Не автор", null, "Не автор");
        Genre existingGenre = new Genre(3, "Жанр3");
        Book book = new Book(EXISTING_BOOK_ID, "Любое", "Любое",
                notExistingAuthor, Set.of(existingGenre));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(AuthorNotFoundException.class);
    }

    @DisplayName("бросить исключение GenreNotFoundException при попытке обновить книгу с указанием несуществующего " +
            "жанра")
    @Test
    void shouldThrowGenreNotFoundExceptionWhenUpdateWithNonExistingGenre() {
        Author existingAuthor =  new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Genre notExistingGenre = new Genre(4, "Не жанр");
        Book book = new Book(EXISTING_BOOK_ID, "Любое", "Любое",
                existingAuthor, Set.of(notExistingGenre));
        assertThatThrownBy(() -> this.booksDao.update(book)).isInstanceOf(GenreNotFoundException.class);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        Author expectedAuthor = new Author(2, "Имя2", null, "Отчество2", "Фамилия2");
        Genre expectedGenre = new Genre(2, "Жанр2");
        Book expectedBook = new Book(EXISTING_BOOK_ID, "Книга3", "Описание3", expectedAuthor,
                Set.of(expectedGenre));
        Book actualBook = this.booksDao.findById(expectedBook.getId());
        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг")
    @Test
    void shouldReturnExpectedBookList() {
        Author expectedAuthor1 = new Author(1, "Имя1", "Среднее имя1", null, "Фамилия1");
        Author expectedAuthor2 = new Author(2, "Имя2", null, "Отчество2", "Фамилия2");
        Genre expectedGenre1 = new Genre(1, "Жанр1");
        Genre expectedGenre2 = new Genre(2, "Жанр2");
        Book expectedBook1 = new Book(1, "Книга1", "Описание1", expectedAuthor1,
                Set.of(expectedGenre1, expectedGenre2));
        Book expectedBook2 = new Book(2, "Книга2", "Описание2", expectedAuthor1,
                Set.of(expectedGenre1, expectedGenre2));
        Book expectedBook3 = new Book(EXISTING_BOOK_ID, "Книга3", "Описание3", expectedAuthor2,
                Set.of(expectedGenre2));
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
