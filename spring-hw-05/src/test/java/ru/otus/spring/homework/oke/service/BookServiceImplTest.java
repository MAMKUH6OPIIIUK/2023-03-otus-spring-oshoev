package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.homework.oke.SpringHw05Application;
import ru.otus.spring.homework.oke.dao.AuthorsDao;
import ru.otus.spring.homework.oke.dao.BooksDao;
import ru.otus.spring.homework.oke.dao.GenresDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

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

    @MockBean
    AuthorsDao authorsDao;

    @MockBean
    GenresDao genresDao;

    @DisplayName("вызывать метод create у дао с ожидаемым аргументом при создании книги и запросить доп. данные для dto")
    @Test
    public void shouldCreateBookUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        long genreId1 = 1;
        long genreId2 = 4;
        BookRequestDto bookRequestDto = new BookRequestDto(bookTitle, bookDescription, authorId, Set.of(genreId1, genreId2));
        Book expectedBookArg = new Book(bookTitle, bookDescription, authorId, Set.of(genreId1, genreId2));
        Book expectedBookFromBookDao = new Book(bookId, bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Author authorFromAuthorsDao = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(booksDao.create(any())).willReturn(expectedBookFromBookDao);
        given(genresDao.findAllByBookId(anyLong())).willReturn(genresFromGenresDao);
        given(authorsDao.findById(anyLong())).willReturn(authorFromAuthorsDao);

        BookResponseDto actualResult = this.booksService.create(bookRequestDto);

        verify(booksDao, times(1)).create(expectedBookArg);
        verify(authorsDao, times(1)).findById(authorId);
        verify(genresDao, times(1)).findAllByBookId(bookId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("вызывать метод update у дао с ожидаемым аргументом при обновлении книги и запросить доп. данные для dto")
    @Test
    public void shouldUpdateBookUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        long genreId1 = 1;
        long genreId2 = 4;
        BookRequestDto bookRequestDto = new BookRequestDto(bookTitle, bookDescription, authorId, Set.of(genreId1, genreId2));
        Book expectedBookArg = new Book(bookId, bookTitle, bookDescription, authorId, Set.of(genreId1, genreId2));
        Book expectedBookFromBookDao = new Book(bookId, bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Author authorFromAuthorsDao = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(booksDao.update(any())).willReturn(expectedBookFromBookDao);
        given(genresDao.findAllByBookId(anyLong())).willReturn(genresFromGenresDao);
        given(authorsDao.findById(anyLong())).willReturn(authorFromAuthorsDao);

        BookResponseDto actualResult = this.booksService.update(bookId, bookRequestDto);

        verify(booksDao, times(1)).update(expectedBookArg);
        verify(authorsDao, times(1)).findById(authorId);
        verify(genresDao, times(1)).findAllByBookId(bookId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("вызывать метод findById у дао с ожидаемым аргументом")
    @Test
    public void shouldFindByIdUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        long genreId1 = 1;
        long genreId2 = 4;
        Book expectedBookFromBookDao = new Book(bookId, bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Author authorFromAuthorsDao = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(booksDao.findById(anyLong())).willReturn(expectedBookFromBookDao);
        given(genresDao.findAllByBookId(anyLong())).willReturn(genresFromGenresDao);
        given(authorsDao.findById(anyLong())).willReturn(authorFromAuthorsDao);

        BookResponseDto actualResult = this.booksService.findById(bookId);

        verify(booksDao, times(1)).findById(bookId);
        verify(authorsDao, times(1)).findById(authorId);
        verify(genresDao, times(1)).findAllByBookId(bookId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("вызывать метод findAll у дао и возвращать весь полученный список")
    @Test
    public void shouldfindAllUsingDao() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        long genreId1 = 1;
        long genreId2 = 4;
        Book expectedBookFromBookDao = new Book(bookId, bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        List<Book> expectedBooksFromBookDao = Arrays.asList(expectedBookFromBookDao);
        Author authorFromAuthorsDao = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        List<Author> authorsFromAuthorsDao = Arrays.asList(authorFromAuthorsDao);
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);
        BookResponseDto expectedBookDto = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(booksDao.findAll()).willReturn(expectedBooksFromBookDao);
        given(genresDao.findAllUsed()).willReturn(genresFromGenresDao);
        given(authorsDao.findAll()).willReturn(authorsFromAuthorsDao);

        List<BookResponseDto> actualResult = this.booksService.findAll();

        verify(booksDao, times(1)).findAll();
        verify(authorsDao, times(1)).findAll();
        verify(genresDao, times(1)).findAllUsed();
        assertThat(actualResult).containsExactlyInAnyOrder(expectedBookDto);
    }

    @DisplayName("вызывать метод deleteById у дао при удалении книги")
    @Test
    public void shouldDeleteByIdUsingDao() {
        long expectedBookId = 1;
        booksService.deleteById(expectedBookId);
        verify(booksDao, times(1)).deleteById(expectedBookId);
    }
}
