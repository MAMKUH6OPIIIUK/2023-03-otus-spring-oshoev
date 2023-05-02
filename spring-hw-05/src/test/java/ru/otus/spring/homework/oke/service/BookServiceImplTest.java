package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.homework.oke.SpringHw05Application;
import ru.otus.spring.homework.oke.dao.BooksDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Сервис работы с книгами должен ")
@SpringBootTest(classes = {SpringHw05Application.class})
public class BookServiceImplTest {
    @Autowired
    BooksService booksService;

    @MockBean
    BooksDao booksDao;

    @DisplayName("вызывать метод create у дао с ожидаемым аргументом при создании книги")
    @Test
    public void shouldCreateBookUsingDao() {
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author authorArg = new Author(authorId, null, null, null, null);
        Author expectedAuthor = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genreArg1 = new Genre(genreId1, null);
        Genre expectedGenre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genreArg2 = new Genre(genreId2, null);
        Genre expectedGenre2 = new Genre(genreId2, "Жанр4");
        Book expectedBookArg = new Book(0, bookTitle, bookDescription, authorArg, Set.of(genreArg1, genreArg2));
        Book expectedBookFromDao = new Book(1, bookTitle, bookDescription, expectedAuthor,
                Set.of(expectedGenre1, expectedGenre2));

        given(booksDao.create(any())).willReturn(expectedBookFromDao);

        Book actualBook = booksService.create(bookTitle, bookDescription, authorId,
                new long[]{genreId1, genreId2});

        verify(booksDao, times(1)).create(expectedBookArg);
        assertThat(actualBook).isEqualTo(expectedBookFromDao);
    }

    @DisplayName("вызывать метод update у дао с ожидаемым аргументом при обновлении книги")
    @Test
    public void shouldUpdateBookUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author authorArg = new Author(authorId, null, null, null, null);
        Author expectedAuthor = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genreArg1 = new Genre(genreId1, null);
        Genre expectedGenre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genreArg2 = new Genre(genreId2, null);
        Genre expectedGenre2 = new Genre(genreId2, "Жанр4");
        Book expectedBookArg = new Book(bookId, bookTitle, bookDescription, authorArg, Set.of(genreArg1, genreArg2));
        Book expectedBookFromDao = new Book(bookId, bookTitle, bookDescription, expectedAuthor,
                Set.of(expectedGenre1, expectedGenre2));

        given(booksDao.update(any())).willReturn(expectedBookFromDao);

        Book actualBook = booksService.update(bookId, bookTitle, bookDescription, authorId,
                new long[]{genreId1, genreId2});

        verify(booksDao, times(1)).update(expectedBookArg);
        assertThat(actualBook).isEqualTo(expectedBookFromDao);
    }

    @DisplayName("вызывать метод findById у дао с ожидаемым аргументом")
    @Test
    public void shouldFindByIdUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author expectedAuthor = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre expectedGenre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre expectedGenre2 = new Genre(genreId2, "Жанр4");
        Book expectedBookFromDao = new Book(bookId, bookTitle, bookDescription, expectedAuthor,
                Set.of(expectedGenre1, expectedGenre2));
        given(booksDao.findById(anyLong())).willReturn(expectedBookFromDao);

        Book actualBook = booksService.findById(bookId);

        verify(booksDao, times(1)).findById(bookId);
        assertThat(actualBook).isEqualTo(expectedBookFromDao);
    }

    @DisplayName("вызывать метод findAll у дао и возвращать весь полученный список")
    @Test
    public void shouldFindAllUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author expectedAuthor = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre expectedGenre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre expectedGenre2 = new Genre(genreId2, "Жанр4");
        Book expectedBook = new Book(bookId, bookTitle, bookDescription, expectedAuthor,
                Set.of(expectedGenre1, expectedGenre2));
        List<Book> expectedBooksFromDao = Arrays.asList(expectedBook);
        given(booksDao.findAll()).willReturn(expectedBooksFromDao);

        List<Book> actualBooks = booksService.findAll();

        verify(booksDao, times(1)).findAll();
        assertThat(actualBooks).containsExactlyInAnyOrder(expectedBook);
    }

    @DisplayName("вызывать метод deleteById у дао при удалении книги")
    @Test
    public void shouldDeleteByIdUsingDao() {
        long expectedBookId = 1;
        booksService.deleteById(expectedBookId);
        verify(booksDao, times(1)).deleteById(expectedBookId);
    }
}
