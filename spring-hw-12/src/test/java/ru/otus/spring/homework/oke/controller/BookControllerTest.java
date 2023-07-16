package ru.otus.spring.homework.oke.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.otus.spring.homework.oke.configuration.SecurityConfig;
import ru.otus.spring.homework.oke.controller.data.DataGenerator;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.mapper.GenreMapper;
import ru.otus.spring.homework.oke.service.AuthorService;
import ru.otus.spring.homework.oke.service.BookService;
import ru.otus.spring.homework.oke.service.CommentService;
import ru.otus.spring.homework.oke.service.GenreService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Контроллер для работы с книгами ")
@WebMvcTest(BookController.class)
@Import({SecurityConfig.class, BookMapper.class, AuthorMapper.class, GenreMapper.class, CommentMapper.class})
public class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @DisplayName("переадресовать на страницу аутентификации при запросе страницы списка книг без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookListPageWithoutUsername() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при запросе страницы книги без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookPageWithoutUsername() throws Exception {
        Long anyId = 1L;
        mvc.perform(get("/book/{id}", anyId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при запросе страницы редактирования комментария к книге" +
            " без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookCommentEditPageWithoutUsername() throws Exception {
        Long anyId = 1L;
        Long anyCommentId = 1L;
        mvc.perform(get("/book/{id}/comment/{commentId}", anyId, anyCommentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при запросе страницы создания книги без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookCreatePageWithoutUsername() throws Exception {
        mvc.perform(get("/book/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при запросе страницы редактирования книги без логина " +
            "и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookEditPageWithoutUsername() throws Exception {
        Long anyId = 1L;
        mvc.perform(get("/book/edit/{id}", anyId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при попытке создания книги без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookCreatePostWithoutUsername() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(expectedBook);
        creatingBook.setId(null);

        mvc.perform(post("/book/create")
                        .queryParam("title", creatingBook.getTitle())
                        .queryParam("description", creatingBook.getDescription())
                        .queryParam("authorId", creatingBook.getAuthorId().toString())
                        .queryParam("genreIds", creatingBook.getGenreIds().get(0).toString())
                        .queryParam("genreIds", creatingBook.getGenreIds().get(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при попытке редактирования книги без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromBookEditPostWithoutUsername() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto editingBook = bookMapper.mapToBookRequestDto(expectedBook);

        mvc.perform(post("/book/edit")
                        .queryParam("id", editingBook.getId().toString())
                        .queryParam("title", editingBook.getTitle())
                        .queryParam("description", editingBook.getDescription())
                        .queryParam("authorId", editingBook.getAuthorId().toString())
                        .queryParam("genreIds", editingBook.getGenreIds().get(0).toString())
                        .queryParam("genreIds", editingBook.getGenreIds().get(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @DisplayName("переадресовать на страницу аутентификации при попытке удаления книги без логина и пароля")
    @Test
    void shouldRedirectToLoginPageFromDeletePostWithoutUsername() throws Exception {
        Long anyId = 1L;

        mvc.perform(post("/book/delete/{id}", anyId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("возвращать ожидаемые view и модель с ожидаемым списком книг")
    @Test
    void shouldReturnExpectedBooksTableForm() throws Exception {
        List<BookResponseDto> expectedBooks = DataGenerator.getAllBooks();

        given(bookService.findAll()).willReturn(expectedBooks);

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", expectedBooks));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("возвращать ожидаемые view и модель с ожидаемой информацией о книге")
    @Test
    void shouldReturnExpectedBookForm() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        Long expectedBookId = expectedBook.getId();
        List<CommentResponseDto> expectedComments = DataGenerator.getFirstBookAllComments();

        given(bookService.findById(expectedBookId)).willReturn(expectedBook);
        given(commentService.findByBookId(expectedBookId)).willReturn(expectedComments);

        mvc.perform(get("/book/{id}", expectedBookId))
                .andExpect(status().isOk())
                .andExpect(view().name("book-info"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments))
                .andExpect(model().attribute("creatingComment", new CommentRequestDto()));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("не менять содержимое атрибута создаваемого комментария при запросе страницы с информацией о книге, " +
            "если он есть в FlashMap")
    @Test
    void shouldNotChangeCreatingCommentIfPresentInModel() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        Long expectedBookId = expectedBook.getId();
        List<CommentResponseDto> expectedComments = DataGenerator.getFirstBookAllComments();
        CommentRequestDto flashCommentAttribute = new CommentRequestDto("Некорректный текст комментария",
                expectedBookId);

        given(bookService.findById(expectedBookId)).willReturn(expectedBook);
        given(commentService.findByBookId(expectedBookId)).willReturn(expectedComments);

        mvc.perform(get("/book/{id}", expectedBookId)
                        .flashAttr("creatingComment", flashCommentAttribute))
                .andExpect(status().isOk())
                .andExpect(view().name("book-info"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments))
                .andExpect(model().attribute("creatingComment", flashCommentAttribute));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("возвращать ожидаемые view и модель для редактирования комментария книги")
    @Test
    void shouldReturnExpectedCommentEditingForm() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        Long expectedBookId = expectedBook.getId();
        List<CommentResponseDto> expectedComments = DataGenerator.getFirstBookAllComments();
        CommentResponseDto editingCommentFromService = DataGenerator.getSecondCommentForFirstBook();
        CommentRequestDto expectedEditingComment = DataGenerator.getSecondEditingCommentForFirstBook();
        Long expectedCommentId = expectedEditingComment.getId();

        given(bookService.findById(expectedBookId)).willReturn(expectedBook);
        given(commentService.findByBookId(expectedBookId)).willReturn(expectedComments);
        given(commentService.findById(expectedCommentId)).willReturn(editingCommentFromService);

        mvc.perform(get("/book/{id}/comment/edit/{commentId}", expectedBookId, expectedCommentId))
                .andExpect(status().isOk())
                .andExpect(view().name("book-info-comment-edit"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments))
                .andExpect(model().attribute("editingComment", expectedEditingComment));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("не менять содержимое атрибута редактируемого комментария при запросе страницы редактирования " +
            "комментария, если он есть в FlashMap")
    @Test
    void shouldNotChangeEditingCommentIfPresentInModel() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        Long expectedBookId = expectedBook.getId();
        List<CommentResponseDto> expectedComments = DataGenerator.getFirstBookAllComments();
        CommentResponseDto editingCommentFromService = DataGenerator.getSecondCommentForFirstBook();
        CommentRequestDto expectedEditingComment = DataGenerator.getSecondEditingCommentForFirstBook();
        expectedEditingComment.setText("Некорректный текст комментария");
        Long expectedCommentId = expectedEditingComment.getId();

        given(bookService.findById(expectedBookId)).willReturn(expectedBook);
        given(commentService.findByBookId(expectedBookId)).willReturn(expectedComments);
        given(commentService.findById(expectedCommentId)).willReturn(editingCommentFromService);

        mvc.perform(get("/book/{id}/comment/edit/{commentId}", expectedBookId, expectedCommentId)
                        .flashAttr("editingComment", expectedEditingComment))
                .andExpect(status().isOk())
                .andExpect(view().name("book-info-comment-edit"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("comments", expectedComments))
                .andExpect(model().attribute("editingComment", expectedEditingComment));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("возвращать ожидаемые view и модель для создания книги")
    @Test
    void shouldReturnExpectedBookCreationForm() throws Exception {
        BookRequestDto expectedBook = new BookRequestDto();
        List<AuthorFullNameDto> expectedAuthors = DataGenerator.getAllAuthors();
        List<GenreResponseDto> expectedGenres = DataGenerator.getAllGenres();

        given(authorService.findAll()).willReturn(expectedAuthors);
        given(genreService.findAll()).willReturn(expectedGenres);

        mvc.perform(get("/book/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("не менять содержимое создаваемой книги, если она есть в FlashMap, при запросе страницы создания " +
            "книги")
    @Test
    void shouldNotChangeCreatingBookIfPresentInModel() throws Exception {
        BookResponseDto book = DataGenerator.getFirstBook();
        BookRequestDto expectedCreatingBook = bookMapper.mapToBookRequestDto(book);
        expectedCreatingBook.setTitle("Некорректное наименование");
        List<AuthorFullNameDto> expectedAuthors = DataGenerator.getAllAuthors();
        List<GenreResponseDto> expectedGenres = DataGenerator.getAllGenres();

        given(authorService.findAll()).willReturn(expectedAuthors);
        given(genreService.findAll()).willReturn(expectedGenres);

        mvc.perform(get("/book/create")
                        .flashAttr("book", expectedCreatingBook))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", expectedCreatingBook))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("корректно добавлять новую корректную книгу и переадресовывать на список книг")
    @Test
    void shouldAddValidBook() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(expectedBook);
        creatingBook.setId(null);

        given(bookService.create(any())).willReturn(expectedBook);

        mvc.perform(post("/book/create")
                        .queryParam("title", creatingBook.getTitle())
                        .queryParam("description", creatingBook.getDescription())
                        .queryParam("authorId", creatingBook.getAuthorId().toString())
                        .queryParam("genreIds", creatingBook.getGenreIds().get(0).toString())
                        .queryParam("genreIds", creatingBook.getGenreIds().get(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1)).create(creatingBook);
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("переадресовать обратно на страницу создания книги, если переданные параметры новой книги некорректны")
    @Test
    void shouldRejectInvalidBookCreation() throws Exception {
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(DataGenerator.getFirstBook());
        StringBuilder invalidTooLongTitle = new StringBuilder();
        for (int i = 0; i <= 125; i++) {
            invalidTooLongTitle.append("long");
        }
        creatingBook.setId(null);
        creatingBook.setTitle(invalidTooLongTitle.toString());
        creatingBook.setGenreIds(Collections.EMPTY_LIST);

        mvc.perform(post("/book/create")
                        .queryParam("title", creatingBook.getTitle())
                        .queryParam("description", creatingBook.getDescription())
                        .queryParam("authorId", creatingBook.getAuthorId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/create"))
                .andExpect(flash().attribute("book", creatingBook))
                .andExpect(flash().attributeExists(BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("возвращать ожидаемые view и модель для редактирования книги")
    @Test
    void shouldReturnExpectedBookEditingForm() throws Exception {
        Long expectedBookId = 1L;
        BookResponseDto book = DataGenerator.getFirstBook();
        BookRequestDto expectedBook = bookMapper.mapToBookRequestDto(book);
        List<AuthorFullNameDto> expectedAuthors = DataGenerator.getAllAuthors();
        List<GenreResponseDto> expectedGenres = DataGenerator.getAllGenres();

        given(authorService.findAll()).willReturn(expectedAuthors);
        given(genreService.findAll()).willReturn(expectedGenres);
        given(bookService.findById(expectedBookId)).willReturn(book);

        mvc.perform(get("/book/edit/{id}", expectedBookId))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", expectedBook))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("не менять содержимое редактируемой книги, если она есть в FlashMap, при запросе страницы " +
            "редактирования книги")
    @Test
    void shouldNotChangeEditingBookIfPresentInModel() throws Exception {
        Long expectedBookId = 1L;
        BookResponseDto book = DataGenerator.getFirstBook();
        BookRequestDto expectedEditingBook = bookMapper.mapToBookRequestDto(book);
        expectedEditingBook.setTitle("Некорректное наименование");
        List<AuthorFullNameDto> expectedAuthors = DataGenerator.getAllAuthors();
        List<GenreResponseDto> expectedGenres = DataGenerator.getAllGenres();

        given(authorService.findAll()).willReturn(expectedAuthors);
        given(genreService.findAll()).willReturn(expectedGenres);
        given(bookService.findById(expectedBookId)).willReturn(book);

        mvc.perform(get("/book/edit/{id}", expectedBookId)
                        .flashAttr("book", expectedEditingBook))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-book"))
                .andExpect(model().attribute("book", expectedEditingBook))
                .andExpect(model().attribute("authors", expectedAuthors))
                .andExpect(model().attribute("genres", expectedGenres));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("корректно обновлять книгу при получении валидной информации и переадресовывать на список книг")
    @Test
    void shouldUpdateValidBook() throws Exception {
        BookResponseDto expectedBook = DataGenerator.getFirstBook();
        BookRequestDto editingBook = bookMapper.mapToBookRequestDto(expectedBook);

        doNothing().when(bookService).update(any());

        mvc.perform(post("/book/edit")
                        .queryParam("id", editingBook.getId().toString())
                        .queryParam("title", editingBook.getTitle())
                        .queryParam("description", editingBook.getDescription())
                        .queryParam("authorId", editingBook.getAuthorId().toString())
                        .queryParam("genreIds", editingBook.getGenreIds().get(0).toString())
                        .queryParam("genreIds", editingBook.getGenreIds().get(1).toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1)).update(editingBook);
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("переадресовать обратно на страницу редактирования книги, если новые переданные параметры книги " +
            "некорректны")
    @Test
    void shouldRejectInvalidBookUpdate() throws Exception {
        BookRequestDto creatingBook = bookMapper.mapToBookRequestDto(DataGenerator.getFirstBook());
        StringBuilder invalidTooLongTitle = new StringBuilder();
        for (int i = 0; i <= 125; i++) {
            invalidTooLongTitle.append("long");
        }
        creatingBook.setTitle(invalidTooLongTitle.toString());
        creatingBook.setGenreIds(Collections.EMPTY_LIST);

        mvc.perform(post("/book/edit")
                        .queryParam("id", creatingBook.getId().toString())
                        .queryParam("title", creatingBook.getTitle())
                        .queryParam("description", creatingBook.getDescription())
                        .queryParam("authorId", creatingBook.getAuthorId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/edit/1"))
                .andExpect(flash().attribute("book", creatingBook))
                .andExpect(flash().attributeExists(BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("корректно обрабатывать запрос на удаление книги")
    @Test
    void shouldCorrectDeleteBook() throws Exception {
        Long expectedBookId = 1L;

        doNothing().when(bookService).deleteById(expectedBookId);

        mvc.perform(post("/book/delete/{id}", expectedBookId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1)).deleteById(expectedBookId);
    }

}
