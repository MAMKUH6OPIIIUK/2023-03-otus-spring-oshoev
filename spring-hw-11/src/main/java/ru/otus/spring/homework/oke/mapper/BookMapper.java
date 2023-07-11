package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;
import ru.otus.spring.homework.oke.dto.GenreDto;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookCreateDto bookCreateDto, Author author) {
        Book book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setDescription(bookCreateDto.getDescription());
        book.setAuthor(author);
        List<Genre> genres = bookCreateDto.getGenres().stream().map(genreMapper::mapToGenre).toList();
        book.setGenres(genres);
        return book;
    }

    public Book mergeBookInfo(Book book, BookUpdateDto bookRequestDto, Author newAuthor) {
        Book mergedBook = new Book();
        mergedBook.setId(book.getId());
        mergedBook.setTitle(bookRequestDto.getTitle());
        mergedBook.setDescription(bookRequestDto.getDescription());
        mergedBook.setAuthor(newAuthor);
        List<Genre> newGenres = bookRequestDto.getGenres().stream().map(genreMapper::mapToGenre).toList();
        mergedBook.setGenres(newGenres);
        return mergedBook;
    }

    public BookResponseDto mapToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        Author author = book.getAuthor();
        AuthorFullNameDto authorDto = authorMapper.mapToAuthorFullNameDto(author);
        dto.setAuthor(authorDto);
        List<Genre> genres = book.getGenres();
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreDto).toList());
        return dto;
    }

    public BookCreateDto mapToBookCreateDto(BookResponseDto book) {
        BookCreateDto dto = new BookCreateDto();
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        AuthorFullNameDto bookAuthor = book.getAuthor();
        dto.setAuthorId(bookAuthor.getId());
        List<GenreDto> genres = book.getGenres();
        dto.setGenres(genres);
        return dto;
    }

    public BookUpdateDto mapToBookUpdateDto(BookResponseDto book) {
        BookUpdateDto dto = new BookUpdateDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        AuthorFullNameDto bookAuthor = book.getAuthor();
        dto.setAuthorId(bookAuthor.getId());
        List<GenreDto> genres = book.getGenres();
        dto.setGenres(genres);
        return dto;
    }
}
