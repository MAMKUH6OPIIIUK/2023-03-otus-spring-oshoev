package ru.otus.spring.homework.oke.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.homework.oke.SpringHw07Application;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.GenreRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Сервис работы с книгами должен ")
@SpringBootTest(classes = {SpringHw07Application.class})
public class BookServiceImplTest {
    @Autowired
    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @MockBean
    AuthorRepository authorRepository;

    @MockBean
    GenreRepository genreRepository;

    @DisplayName("вызывать метод save у репозитория с ожидаемым аргументом при создании книги и запросить доп. " +
            "данные для dto")
    @Test
    public void shouldCreateBookUsingRepository() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author author = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genre2 = new Genre(genreId2, "Жанр4");
        BookRequestDto bookRequestDto = new BookRequestDto(bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Book expectedBookArg = new Book(bookTitle, bookDescription, author, Set.of(genre1, genre2));
        Book expectedBookFromBookDao = new Book(bookId, bookTitle, bookDescription, author,
                Set.of(genre1, genre2));
        Optional<Author> authorFromAuthorsDao = Optional.of(author);
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(bookRepository.save(any())).willReturn(expectedBookFromBookDao);
        given(genreRepository.findByIdIn(any())).willReturn(genresFromGenresDao);
        given(authorRepository.findById(anyLong())).willReturn(authorFromAuthorsDao);

        BookResponseDto actualResult = this.bookService.create(bookRequestDto);

        verify(bookRepository, times(1)).save(ArgumentMatchers.refEq(expectedBookArg));
        verify(authorRepository, times(1)).findById(authorId);
        verify(genreRepository, times(1)).findByIdIn(bookRequestDto.getGenreIds());
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("вызывать метод save у репозитория с ожидаемым аргументом при обновлении книги и запросить доп. " +
            "данные для dto")
    @Test
    public void shouldUpdateBookUsingRepository() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author author = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genre1 = new Genre(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genre2 = new Genre(genreId2, "Жанр4");
        BookRequestDto bookRequestDto = new BookRequestDto(bookId, bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Set<Genre> bookGenres = new HashSet<>();
        bookGenres.add(genre1);
        bookGenres.add(genre2);
        Book expectedBookArg = new Book(bookId, bookTitle, bookDescription, author, bookGenres);
        Book expectedBookFromBookDao = expectedBookArg;
        Optional<Author> authorFromAuthorsDao = Optional.of(author);
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);

        given(bookRepository.save(any())).willReturn(expectedBookFromBookDao);
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(expectedBookArg));
        given(genreRepository.findByIdIn(any())).willReturn(genresFromGenresDao);
        given(authorRepository.findById(anyLong())).willReturn(authorFromAuthorsDao);

        this.bookService.update(bookRequestDto);

        verify(bookRepository, times(1)).save(ArgumentMatchers.refEq(expectedBookArg));
        verify(authorRepository, times(1)).findById(authorId);
        verify(genreRepository, times(1)).findByIdIn(bookRequestDto.getGenreIds());
    }

    @DisplayName("вызывать метод findById у репозитория с ожидаемым аргументом")
    @Test
    public void shouldFindByIdUsingRepository() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author author = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        Book expectedBook = new Book(bookId, bookTitle, bookDescription, author, Set.of(genre1, genre2));
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(bookRepository.findById(anyLong())).willReturn(Optional.of(expectedBook));

        BookResponseDto actualResult = this.bookService.findById(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @DisplayName("вызывать метод findAll у репозитория и возвращать весь полученный список книг")
    @Test
    public void shouldFindAllUsingRepository() {
        long bookId = 1;
        String bookTitle = "Книга4";
        String bookDescription = "Описание4";
        long authorId = 2;
        Author author = new Author(authorId, "Имя2", null, "Отчество2", "Фамилия2");
        AuthorResponseDto authorDto = new AuthorResponseDto(authorId, "Имя2", null,
                "Отчество2", "Фамилия2");
        long genreId1 = 1;
        Genre genre1 = new Genre(genreId1, "Жанр1");
        GenreResponseDto genreDto1 = new GenreResponseDto(genreId1, "Жанр1");
        long genreId2 = 4;
        Genre genre2 = new Genre(genreId2, "Жанр4");
        GenreResponseDto genreDto2 = new GenreResponseDto(genreId2, "Жанр4");
        Book expectedBook = new Book(bookId, bookTitle, bookDescription, author, Set.of(genre1, genre2));
        BookResponseDto expectedResult = new BookResponseDto(bookId, bookTitle, bookDescription, authorDto,
                Set.of(genreDto1, genreDto2));

        given(bookRepository.findAll()).willReturn(List.of(expectedBook));

        List<BookResponseDto> actualResult = this.bookService.findAll();

        verify(bookRepository, times(1)).findAll();
        assertThat(actualResult).containsExactlyInAnyOrder(expectedResult);
    }

    @DisplayName("вызывать метод deleteById у репозитория при удалении книги")
    @Test
    public void shouldDeleteByIdUsingRepository() {
        long expectedBookId = 1;
        bookService.deleteById(expectedBookId);
        verify(bookRepository, times(1)).deleteById(expectedBookId);
    }

    @DisplayName("бросить исключение NotFoundException при попытке создать книгу с несуществующим автором")
    @Test
    public void shouldThrowNotFoundExpectionWhenCreateWithNonExistingAuthor() {
        long nonExistingAuthorId = 4;
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", nonExistingAuthorId, new HashSet<>());
        given(authorRepository.findById(nonExistingAuthorId)).willReturn(Optional.ofNullable(null));
        given(genreRepository.findByIdIn(any())).willReturn(Collections.EMPTY_LIST);
        assertThatThrownBy(() -> bookService.create(requestDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке создать книгу с несуществующим жанром")
    @Test
    public void shouldThrowNotFoundExpectionWhenCreateWithNonExistingGenre() {
        long existingGenreId = 1;
        long nonExistingGenreId = 2;
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", 1L,
                Set.of(existingGenreId, nonExistingGenreId));
        Author authorFromRepository = new Author(1L, "Любое", "Любое", null, "Любое");
        Genre existingGenre = new Genre(existingGenreId, "Любое");
        List<Genre> genresFromRepository = List.of(existingGenre);
        given(authorRepository.findById(anyLong())).willReturn(Optional.of(authorFromRepository));
        given(genreRepository.findByIdIn(any())).willReturn(genresFromRepository);
        assertThatThrownBy(() -> bookService.create(requestDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке обновить несуществующую книгу")
    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateNonExistingBook() {
        long nonExistingBookId = 3;
        BookRequestDto requestDto = new BookRequestDto(nonExistingBookId, "Любое", "Любое", 1L, Set.of(1L));
        given(bookRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> {
            bookService.update(requestDto);
        })
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке обновить книгу с указанием несуществующего автора")
    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateWithNonExistingAuthor() {
        long existingBookId = 1;
        Author existingAuthor = new Author(1L, "Любое", null, "Любое", "Любая");
        Book bookFromRepository = new Book(existingBookId, "Любое", "Любое", existingAuthor, new HashSet<>());
        long nonExistingAuthorId = 4;
        BookRequestDto requestDto = new BookRequestDto(existingBookId, "Любое", "Любое",
                nonExistingAuthorId, new HashSet<>());

        given(authorRepository.findById(nonExistingAuthorId)).willReturn(Optional.ofNullable(null));
        given(genreRepository.findByIdIn(any())).willReturn(Collections.EMPTY_LIST);
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(bookFromRepository));

        assertThatThrownBy(() -> bookService.update(requestDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке обновить книгу с указанием несуществующего жанра")
    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateWithNonExistingGenre() {
        long existingBookId = 1;
        Author existingAuthor = new Author(1L, "Любое", null, "Любое", "Любая");
        Book bookFromRepository = new Book(existingBookId, "Любое", "Любое", existingAuthor, new HashSet<>());
        long existingGenreId = 1;
        long nonExistingGenreId = 2;
        BookRequestDto requestDto = new BookRequestDto(existingBookId,"Любое", "Любое", 1L,
                Set.of(existingGenreId, nonExistingGenreId));
        Genre existingGenre = new Genre(existingGenreId, "Любое");
        List<Genre> genresFromRepository = List.of(existingGenre);

        given(authorRepository.findById(anyLong())).willReturn(Optional.of(existingAuthor));
        given(genreRepository.findByIdIn(any())).willReturn(genresFromRepository);
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(bookFromRepository));

        assertThatThrownBy(() -> bookService.update(requestDto)).isInstanceOf(NotFoundException.class);
    }

}
