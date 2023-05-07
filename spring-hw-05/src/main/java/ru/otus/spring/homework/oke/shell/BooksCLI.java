package ru.otus.spring.homework.oke.shell;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.formatters.BookFormatter;
import ru.otus.spring.homework.oke.service.BooksService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ShellComponent
@RequiredArgsConstructor
public class BooksCLI {
    private final BooksService booksService;

    private final BookFormatter bookFormatter;

    @ShellMethod(value = "Добавить новую книгу", key = {"cb", "create book"})
    public String createBook(@ShellOption(help = "Наименование книги", value = {"t", "title"})
                             @Size(min = 1, max = 500)
                                     String title,
                             @ShellOption(help = "Описание/аннотация книги", value = {"d", "description"},
                                     defaultValue = ShellOption.NULL)
                             @Size(max = 1000)
                                     String description,
                             @ShellOption(help = "Идентификатор автора", value = {"ai", "authorId"})
                             @Positive
                                     long authorId,
                             @ShellOption(help = "Идентификаторы жанров книги", value = {"gi", "genreId"})
                                     long[] genreIds) {
        Set<Long> genresSet = new HashSet<>();
        for (long genreId : genreIds) {
            genresSet.add(genreId);
        }
        BookRequestDto bookRequestDto = new BookRequestDto(title, description, authorId, genresSet);
        BookResponseDto createdBook = this.booksService.create(bookRequestDto);
        return "Успешно добавлена книга! " + this.bookFormatter.formatBook(createdBook, 0);
    }

    @ShellMethod(value = "Обновить данные о книге", key = {"ub", "update book"})
    public String updateBook(@ShellOption(help = "Идентификатор существующей книги", value = {"i", "id"})
                             @Positive
                                     long id,
                             @ShellOption(help = "Наименование книги", value = {"t", "title"})
                             @Size(min = 1, max = 500)
                                     String title,
                             @ShellOption(help = "Описание/аннотация книги", value = {"d", "description"},
                                     defaultValue = ShellOption.NULL)
                             @Size(min = 1, max = 1000)
                                     String description,
                             @ShellOption(help = "Идентификатор автора", value = {"ai", "authorId"})
                             @Positive
                                     long authorId,
                             @ShellOption(help = "Идентификаторы жанров книги", value = {"gi", "genreId"})
                                     long[] genreIds) {
        Set<Long> genresSet = new HashSet<>();
        for (long genreId : genreIds) {
            genresSet.add(genreId);
        }
        BookRequestDto bookRequestDto = new BookRequestDto(title, description, authorId, genresSet);
        BookResponseDto updatedBook = this.booksService.update(id, bookRequestDto);
        return "Успешно обновлена книга! " + this.bookFormatter.formatBook(updatedBook, 0);
    }

    @ShellMethod(value = "Удалить книгу", key = {"rb", "remove book"})
    public void removeBook(@ShellOption(help = "Идентификатор существующей книги", value = {"i", "id"})
                             @Positive long id) {
        this.booksService.deleteById(id);
    }

    @ShellMethod(value = "Получить все существующие книги", key = {"gab", "get all books"})
    public String getAllBooks() {
        List<BookResponseDto> books = this.booksService.findAll();
        return this.bookFormatter.formatBooks(books, 0);
    }

    @ShellMethod(value = "Получить книгу по её идентификатору", key = {"gb", "get book"})
    public String getBookById(long id) {
        BookResponseDto book = this.booksService.findById(id);
        return this.bookFormatter.formatBook(book, 0);
    }
}
