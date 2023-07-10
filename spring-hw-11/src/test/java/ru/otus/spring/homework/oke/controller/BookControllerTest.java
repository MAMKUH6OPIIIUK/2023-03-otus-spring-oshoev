package ru.otus.spring.homework.oke.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.homework.oke.controller.data.DataGenerator;
import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;
import ru.otus.spring.homework.oke.dto.ErrorDto;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.mapper.GenreMapper;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("REST-контроллер для работы с книгами должен ")
@WebFluxTest({BookController.class, ErrorsController.class})
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class BookControllerTest {
    private static final String TITLE_TOO_LONG_ERROR = "Поле заголовка книги должно содержать от 1 до 500 символов";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private BookMapper bookMapper;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private CommentRepository commentRepository;

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectetBookById() {
        String bookId = "1";
        Book expectedBook = DataGenerator.getFirstBook();
        BookResponseDto expectedBody = bookMapper.mapToBookResponseDto(expectedBook);

        given(bookRepository.findById(bookId)).willReturn(Mono.just(expectedBook));

        BookResponseDto actualBook = webTestClient
                .get().uri("/api/book/" + bookId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BookResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBody);
    }

    @DisplayName("возвращать ожидаемый список всех книг")
    @Test
    void shouldReturnExpectedBooks() {
        List<Book> expectedBooks = DataGenerator.getAllBooks();

        List<BookResponseDto> expectedBody = DataGenerator.getAllBooks().stream()
                .map(bookMapper::mapToBookResponseDto)
                .toList();

        given(bookRepository.findAll()).willReturn(Flux.fromIterable(expectedBooks));

        List<BookResponseDto> actualBooks = webTestClient.get().uri("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookResponseDto.class).hasSize(expectedBooks.size())
                .returnResult()
                .getResponseBody();
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedBody);
    }

    @DisplayName("успешно добавлять новую корректную книгу и возвращать статус 201")
    @Test
    void shouldCreateCorrectBook() {
        Book expectedBook = DataGenerator.getFirstBook();
        Author expectedAuthor = expectedBook.getAuthor();
        BookResponseDto expectedResponseBody = bookMapper.mapToBookResponseDto(expectedBook);
        BookCreateDto creatingBook = bookMapper.mapToBookCreateDto(expectedResponseBody);

        given(authorRepository.findById(expectedAuthor.getId())).willReturn(Mono.just(expectedAuthor));
        given(bookRepository.save(any())).willReturn(Mono.just(expectedBook));

        BookResponseDto actualBook = webTestClient.post().uri("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(creatingBook), BookCreateDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BookResponseDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedResponseBody);
        verify(authorRepository, times(1)).findById(expectedAuthor.getId());
        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isNull();
        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedBook);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке создания некорректно заполненнной книги")
    @Test
    void shouldRejectInvalidBookCreate() {
        BookCreateDto creatingBook = bookMapper.mapToBookCreateDto(
                bookMapper.mapToBookResponseDto(DataGenerator.getFirstBook()));
        creatingBook.setTitle("title".repeat(101));
        ErrorDto actualError = webTestClient.post().uri("/api/book")
                .accept(MediaType.APPLICATION_JSON)
                .cookie("locale", "ru")
                .body(Mono.just(creatingBook), BookCreateDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(actualError.getFields()).containsEntry("title", List.of(TITLE_TOO_LONG_ERROR));
    }

    @DisplayName("успешно обновлять существующую книгу новыми корректными данными и возвращать статус 200")
    @Test
    void shouldUpdateCorrectBook() {
        Book expectedBook = DataGenerator.getFirstBook();
        Author expectedAuthor = expectedBook.getAuthor();
        BookUpdateDto updatingBook = bookMapper.mapToBookUpdateDto(bookMapper.mapToBookResponseDto(expectedBook));

        given(authorRepository.findById(expectedAuthor.getId())).willReturn(Mono.just(expectedAuthor));
        given(bookRepository.save(any())).willReturn(Mono.just(expectedBook));
        given(bookRepository.findById(expectedBook.getId())).willReturn(Mono.just(expectedBook));

        webTestClient.put().uri("/api/book/" + expectedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatingBook), BookUpdateDto.class)
                .exchange()
                .expectStatus().isOk();

        verify(authorRepository, times(1)).findById(expectedAuthor.getId());
        verify(bookRepository, times(1)).findById(expectedBook.getId());
        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке обновить книгу с указанием некорректного " +
            "заголовка")
    @Test
    void shouldRejectInvalidBookUpdate() {
        Book book = DataGenerator.getFirstBook();
        BookUpdateDto updatingBook = bookMapper.mapToBookUpdateDto(bookMapper.mapToBookResponseDto(book));
        updatingBook.setTitle("title".repeat(101));

        ErrorDto actualError = webTestClient.put().uri("/api/book/" + book.getId())
                .accept(MediaType.APPLICATION_JSON)
                .cookie("locale", "ru")
                .body(Mono.just(updatingBook), BookCreateDto.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(actualError.getFields()).containsEntry("title", List.of(TITLE_TOO_LONG_ERROR));
    }

    @DisplayName("удалять книгу по её идентификатору и комментарии к ней")
    @Test
    void shouldCorrectDeleteBook() {
        String bookId = "1";

        given(bookRepository.deleteById(anyString())).willReturn(Mono.empty());
        given(commentRepository.deleteByBookId(anyString())).willReturn(Mono.empty());

        webTestClient.delete().uri("/api/book/" + bookId)
                .exchange()
                .expectStatus().isNoContent();

        verify(commentRepository, times(1)).deleteByBookId(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
