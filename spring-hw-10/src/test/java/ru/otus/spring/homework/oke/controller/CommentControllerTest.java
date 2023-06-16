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
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.service.CommentService;

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

@DisplayName("REST-контроллер для работы с комментариями должен ")
@WebMvcTest({CommentController.class, ErrorsController.class})
@Import(CommentMapper.class)
public class CommentControllerTest {
    private final static String TEXT_TOO_LONG_ERROR = "Текст комментария может содержать от 1 до 1000 символов";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @DisplayName("возвращать ожидаемый ответ со списком всех комментариев книги")
    @Test
    void shouldReturnExpectedBookComments() throws Exception {
        Long bookId = 1L;
        List<CommentResponseDto> expectedComments = DataGenerator.getFirstBookAllComments();

        given(commentService.findByBookId(bookId)).willReturn(expectedComments);

        mvc.perform(get("/api/comment")
                        .queryParam("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedComments)));
    }

    @DisplayName("успешно добавлять новый корректный комментарий и возвращать статус 201")
    @Test
    void shouldCreateCorrectComment() throws Exception {
        CommentResponseDto expectedComment = DataGenerator.getFirstCommentForFirstBook();
        CommentRequestDto creatingComment = commentMapper.mapToCommentRequestDto(expectedComment, 1L);
        creatingComment.setId(null);

        given(commentService.create(any())).willReturn(expectedComment);

        mvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(creatingComment)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expectedComment)));
        verify(commentService, times(1)).create(creatingComment);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке создания комментария с некорректно " +
            "заполненным текстом")
    @Test
    void shouldRejectInvalidCommentCreate() throws Exception {
        CommentRequestDto creatingComment = commentMapper.mapToCommentRequestDto(DataGenerator
                .getFirstCommentForFirstBook(), 1L);
        creatingComment.setId(null);
        creatingComment.setText("text".repeat(251));

        mvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(creatingComment))
                        .cookie(new Cookie("locale", "ru")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.text", hasItems(TEXT_TOO_LONG_ERROR)));
    }

    @DisplayName("успешно обновлять существующий комментарий новыми корректными данными и возвращать статус 200")
    @Test
    void shouldUpdateCorrectComment() throws Exception {
        CommentResponseDto expectedComment = DataGenerator.getFirstCommentForFirstBook();
        CommentRequestDto updatingComment = commentMapper.mapToCommentRequestDto(expectedComment, null);
        String commentId = updatingComment.getId().toString();

        doNothing().when(commentService).update(any());

        mvc.perform(put("/api/comment/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatingComment)))
                .andExpect(status().isOk());
        verify(commentService, times(1)).update(updatingComment);
    }

    @DisplayName("возвращать статус 400 и dto со списком ошибок при попытке обновить комментарий с указанием " +
            "некорректно заполненного текста")
    @Test
    void shouldRejectInvalidCommentUpdate() throws Exception {
        CommentRequestDto updatingComment = commentMapper.mapToCommentRequestDto(DataGenerator
                .getFirstCommentForFirstBook(), 1L);
        updatingComment.setText("text".repeat(251));
        String commentId = updatingComment.getId().toString();

        mvc.perform(put("/api/comment/" + commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatingComment))
                        .cookie(new Cookie("locale", "ru")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.text", hasItems(TEXT_TOO_LONG_ERROR)));
    }

    @DisplayName("удалять комментарий по его идентификатору")
    @Test
    void shouldCorrectDeleteComment() throws Exception {
        Long commentId = 1L;

        doNothing().when(commentService).deleteById(any());

        mvc.perform(delete("/api/comment/" + commentId))
                .andExpect(status().isNoContent());
        verify(commentService, times(1)).deleteById(commentId);
    }
}
