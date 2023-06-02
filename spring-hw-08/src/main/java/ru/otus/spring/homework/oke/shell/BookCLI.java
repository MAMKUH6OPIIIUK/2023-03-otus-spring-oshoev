package ru.otus.spring.homework.oke.shell;

import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.GenreDto;
import ru.otus.spring.homework.oke.formatter.BookFormatter;
import ru.otus.spring.homework.oke.service.BookService;

import java.util.LinkedList;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class BookCLI {
    private final BookService bookService;

    private final BookFormatter bookFormatter;

    @ShellMethod(value = "Добавить новую книгу", key = {"cb", "create book"})
    public String createBook(@ShellOption(help = "Наименование книги", value = {"t", "title"})
                             @Size(min = 1)
                                     String title,
                             @ShellOption(help = "Описание/аннотация книги", value = {"d", "description"},
                                     defaultValue = ShellOption.NULL)
                                     String description,
                             @ShellOption(help = "Идентификатор автора", value = {"ai", "authorId"})
                                     String authorId,
                             @ShellOption(help = "Наименования жанров книги", value = {"gn", "genreName"})
                                     String[] genreNames) {
        List<GenreDto> genres = new LinkedList<>();
        for (String genreName : genreNames) {
            genres.add(new GenreDto(genreName));
        }
        BookRequestDto bookRequestDto = new BookRequestDto(title, description, authorId, genres);
        BookResponseDto createdBook = this.bookService.create(bookRequestDto);
        return "Успешно добавлена книга! " + this.bookFormatter.formatBook(createdBook, 0);
    }

    @ShellMethod(value = "Обновить данные о книге", key = {"ub", "update book"})
    public String updateBook(@ShellOption(help = "Идентификатор существующей книги", value = {"i", "id"})
                                     String id,
                             @ShellOption(help = "Наименование книги", value = {"t", "title"})
                             @Size(min = 1)
                                     String title,
                             @ShellOption(help = "Описание/аннотация книги", value = {"d", "description"},
                                     defaultValue = ShellOption.NULL)
                                     String description,
                             @ShellOption(help = "Идентификатор автора", value = {"ai", "authorId"})
                                     String authorId,
                             @ShellOption(help = "Наименования жанров книги", value = {"gn", "genreName"})
                                         String[] genreNames) {
        List<GenreDto> genres = new LinkedList<>();
        for (String genreName : genreNames) {
            genres.add(new GenreDto(genreName));
        }
        BookRequestDto bookRequestDto = new BookRequestDto(id, title, description, authorId, genres);
        this.bookService.update(bookRequestDto);
        return "Книга успешно обновлена!";
    }

    @ShellMethod(value = "Удалить книгу", key = {"rb", "remove book"})
    public void removeBook(@ShellOption(help = "Идентификатор существующей книги", value = {"i", "id"})
                           String id) {
        this.bookService.deleteById(id);
    }

    @ShellMethod(value = "Получить все существующие книги", key = {"gab", "get all books"})
    public String getAllBooks() {
        List<BookResponseDto> books = this.bookService.findAll();
        return this.bookFormatter.formatBooks(books, 0);
    }

    @ShellMethod(value = "Получить книгу по её идентификатору", key = {"gb", "get book"})
    public String getBookById(String id) {
        BookResponseDto book = this.bookService.findById(id);
        return this.bookFormatter.formatBook(book, 0);
    }
}
