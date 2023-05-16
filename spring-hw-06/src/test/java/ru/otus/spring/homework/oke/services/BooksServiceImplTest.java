package ru.otus.spring.homework.oke.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.homework.oke.SpringHw06Application;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.models.Author;
import ru.otus.spring.homework.oke.models.Book;
import ru.otus.spring.homework.oke.models.Genre;
import ru.otus.spring.homework.oke.repositories.AuthorsRepository;
import ru.otus.spring.homework.oke.repositories.BooksRepository;
import ru.otus.spring.homework.oke.repositories.GenresRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("Сервис работы с книгами должен ")
@SpringBootTest(classes = {SpringHw06Application.class})
public class BooksServiceImplTest {
    @Autowired
    BooksService booksService;

    @MockBean
    BooksRepository booksRepository;

    @MockBean
    AuthorsRepository authorsRepository;

    @MockBean
    GenresRepository genresRepository;

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

        given(booksRepository.save(any())).willReturn(expectedBookFromBookDao);
        given(genresRepository.findByIds(any())).willReturn(genresFromGenresDao);
        given(authorsRepository.findById(anyLong())).willReturn(authorFromAuthorsDao);

        BookResponseDto actualResult = this.booksService.create(bookRequestDto);

        verify(booksRepository, times(1)).save(ArgumentMatchers.refEq(expectedBookArg));
        verify(authorsRepository, times(1)).findById(authorId);
        verify(genresRepository, times(1)).findByIds(bookRequestDto.getGenreIds());
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
        BookRequestDto bookRequestDto = new BookRequestDto(bookTitle, bookDescription, authorId,
                Set.of(genreId1, genreId2));
        Book expectedBookArg = new Book(bookId, bookTitle, bookDescription, author, Set.of(genre1, genre2));
        Book expectedBookFromBookDao = expectedBookArg;
        Optional<Author> authorFromAuthorsDao = Optional.of(author);
        List<Genre> genresFromGenresDao = List.of(genre1, genre2);

        given(booksRepository.save(any())).willReturn(expectedBookFromBookDao);
        given(booksRepository.findById(anyLong())).willReturn(Optional.of(expectedBookArg));
        given(genresRepository.findByIds(any())).willReturn(genresFromGenresDao);
        given(authorsRepository.findById(anyLong())).willReturn(authorFromAuthorsDao);

        this.booksService.update(bookId, bookRequestDto);

        verify(booksRepository, times(1)).save(ArgumentMatchers.refEq(expectedBookArg));
        verify(authorsRepository, times(1)).findById(authorId);
        verify(genresRepository, times(1)).findByIds(bookRequestDto.getGenreIds());
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

        given(booksRepository.findById(anyLong())).willReturn(Optional.of(expectedBook));

        BookResponseDto actualResult = this.booksService.findById(bookId);

        verify(booksRepository, times(1)).findById(bookId);
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

        given(booksRepository.findAll()).willReturn(List.of(expectedBook));

        List<BookResponseDto> actualResult = this.booksService.findAll();

        verify(booksRepository, times(1)).findAll();
        assertThat(actualResult).containsExactlyInAnyOrder(expectedResult);
    }

    @DisplayName("вызывать метод deleteById у репозитория при удалении книги")
    @Test
    public void shouldDeleteByIdUsingRepository() {
        long expectedBookId = 1;
        booksService.deleteById(expectedBookId);
        verify(booksRepository, times(1)).deleteById(expectedBookId);
    }

    @DisplayName("бросить исключение NotFoundException при попытке создать книгу с несуществующим автором")
    @Test
    public void shouldThrowNotFoundExpectionWhenCreateWithNonExistingAuthor() {
        long nonExistingAuthorId = 4;
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", nonExistingAuthorId, new HashSet<>());
        given(authorsRepository.findById(nonExistingAuthorId)).willReturn(Optional.ofNullable(null));
        given(genresRepository.findByIds(any())).willReturn(Collections.EMPTY_LIST);
        assertThatThrownBy(() -> booksService.create(requestDto)).isInstanceOf(NotFoundException.class);
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
        given(authorsRepository.findById(anyLong())).willReturn(Optional.of(authorFromRepository));
        given(genresRepository.findByIds(any())).willReturn(genresFromRepository);
        assertThatThrownBy(() -> booksService.create(requestDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке обновить несуществующую книгу")
    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateNonExistingBook() {
        long nonExistingBookId = 3;
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", 1L, Set.of(1L));
        given(booksRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
        assertThatThrownBy(() -> {
            booksService.update(nonExistingBookId, requestDto);
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
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", nonExistingAuthorId, new HashSet<>());

        given(authorsRepository.findById(nonExistingAuthorId)).willReturn(Optional.ofNullable(null));
        given(genresRepository.findByIds(any())).willReturn(Collections.EMPTY_LIST);
        given(booksRepository.findById(anyLong())).willReturn(Optional.of(bookFromRepository));

        assertThatThrownBy(() -> booksService.update(existingBookId, requestDto)).isInstanceOf(NotFoundException.class);
    }

    @DisplayName("бросить исключение NotFoundException при попытке обновить книгу с указанием несуществующего жанра")
    @Test
    public void shouldThrowNotFoundExceptionWhenUpdateWithNonExistingGenre() {
        long existingBookId = 1;
        Author existingAuthor = new Author(1L, "Любое", null, "Любое", "Любая");
        Book bookFromRepository = new Book(existingBookId, "Любое", "Любое", existingAuthor, new HashSet<>());
        long existingGenreId = 1;
        long nonExistingGenreId = 2;
        BookRequestDto requestDto = new BookRequestDto("Любое", "Любое", 1L,
                Set.of(existingGenreId, nonExistingGenreId));
        Genre existingGenre = new Genre(existingGenreId, "Любое");
        List<Genre> genresFromRepository = List.of(existingGenre);

        given(authorsRepository.findById(anyLong())).willReturn(Optional.of(existingAuthor));
        given(genresRepository.findByIds(any())).willReturn(genresFromRepository);
        given(booksRepository.findById(anyLong())).willReturn(Optional.of(bookFromRepository));

        assertThatThrownBy(() -> booksService.update(existingBookId, requestDto)).isInstanceOf(NotFoundException.class);
    }

}
