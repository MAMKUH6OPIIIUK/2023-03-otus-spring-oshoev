package ru.otus.spring.homework.oke.controller.data;

import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;

import java.util.List;

public class DataGenerator {
    public static AuthorFullNameDto getFirstAuthor() {
        return new AuthorFullNameDto(1L, "Василий Васильевич Пупкин");
    }

    public static AuthorFullNameDto getSecondAuthor() {
        return new AuthorFullNameDto(2L, "Иванов Иван Иванович");
    }

    public static List<AuthorFullNameDto> getAllAuthors() {
        return List.of(getFirstAuthor(), getSecondAuthor());
    }

    public static GenreResponseDto getFirstGenre() {
        return new GenreResponseDto(1L, "Жанр1");
    }

    public static GenreResponseDto getSecondGenre() {
        return new GenreResponseDto(2L, "Жанр2");
    }

    public static GenreResponseDto getThirdGenre() {
        return new GenreResponseDto(3L, "Жанр3");
    }

    public static List<GenreResponseDto> getAllGenres() {
        return List.of(getFirstGenre(), getSecondGenre(), getThirdGenre());
    }

    public static BookResponseDto getFirstBook() {
        return new BookResponseDto(1L, "Книга1", "Описание1", getFirstAuthor(),
                List.of(getFirstGenre(), getSecondGenre()));
    }

    public static BookResponseDto getSecondBook() {
        return new BookResponseDto(2L, "Книга2", "Описание2", getSecondAuthor(),
                List.of(getThirdGenre()));
    }

    public static List<BookResponseDto> getAllBooks() {
        return List.of(getFirstBook(), getSecondBook());
    }

    public static CommentResponseDto getFirstCommentForFirstBook() {
        return new CommentResponseDto(1L, "Коммент1");
    }

    public static CommentResponseDto getSecondCommentForFirstBook() {
        return new CommentResponseDto(2L, "Коммент2");
    }

    public static CommentRequestDto getSecondEditingCommentForFirstBook() {
        return new CommentRequestDto(2L, "Коммент2", 1L);
    }

    public static List<CommentResponseDto> getFirstBookAllComments() {
        return List.of(getFirstCommentForFirstBook(), getSecondCommentForFirstBook());
    }
}
