package ru.otus.spring.homework.oke.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.homework.oke.controller.data.DataGenerator;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.mapper.GenreMapper;
import ru.otus.spring.homework.oke.service.BookService;

import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("REST-контроллер для работы с книгами должен ")
@WebMvcTest({BookController.class, ErrorsController.class})
@Import({BookMapper.class, AuthorMapper.class, GenreMapper.class})
public class BookControllerTest {
    private static final String TITLE_TOO_LONG_ERROR = "Поле заголовка книги должно содержать от 1 до 500 символов";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectetBookById() throws Exception {
        Long bookId = 1L;
        BookResponseDto expectedBook = DataGenerator.getFirstBook();

        given(bookService.findById(bookId)).willReturn(expectedBook);

        mvc.perform(get("/api/book/" + bookId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
    }

    @DisplayName("возвращать ожидаемый список всех книг")
    @Test
    void shouldReturnExpectedBooks() throws Exception {
        List<BookResponseDto> expectedBooks = DataGenerator.getAllBooks();

        given(bookService.findAll()).willReturn(expectedBooks);

        mvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBooks)));
    }

    @DisplayName("успешно добавлять новую корректную книгу и возвращать статус 201")
    @Test
    void shouldCreateCorrectBook() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(expectedBook);
        creatingBook.setId(null);

        given(bookService.create(any())).willReturn(expectedBook);

        mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(creatingBook)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expectedBook)));
        verify(bookService, times(1)).create(creatingBook);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке создания некорректно заполненнной книги")
    @Test
    void shouldRejectInvalidBookCreate() throws Exception {
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(DataGenerator.getFirstBook());
        creatingBook.setId(null);
        creatingBook.setTitle("title".repeat(101));

        mvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(creatingBook))
                        .cookie(new Cookie("locale", "ru")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.title", hasItems(TITLE_TOO_LONG_ERROR)));
    }

    @DisplayName("успешно обновлять существующую книгу новыми корректными данными и возвращать статус 200")
    @Test
    void shouldUpdateCorrectBook() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto updatingBook = bookMapper.mapToBookRequestDto(expectedBook);
        String bookId = updatingBook.getId().toString();

        doNothing().when(bookService).update(any());

        mvc.perform(put("/api/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatingBook)))
                .andExpect(status().isOk());
        verify(bookService, times(1)).update(updatingBook);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке обновить книгу с указанием некорректного " +
            "заголовка")
    @Test
    void shouldRejectInvalidBookUpdate() throws Exception {
        BookRequestDto updatingBook = bookMapper.mapToBookRequestDto(DataGenerator.getFirstBook());
        updatingBook.setTitle("title".repeat(101));
        String bookId = updatingBook.getId().toString();

        mvc.perform(put("/api/book/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatingBook))
                        .cookie(new Cookie("locale", "ru")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.title", hasItems(TITLE_TOO_LONG_ERROR)));
    }

    @DisplayName("удалять книгу по её идентификатору")
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        Long bookId = 1L;

        doNothing().when(bookService).deleteById(any());

        mvc.perform(delete("/api/book/" + bookId))
                .andExpect(status().isOk());
        verify(bookService, times(1)).deleteById(bookId);
    }
}
