package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookRequestDto bookRequestDto, Author author) {
        Book book = new Book();
        book.setId(bookRequestDto.getId());
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(author);
        List<Genre> genres = bookRequestDto.getGenres().stream().map(genreMapper::mapToGenre).toList();
        book.setGenres(genres);
        return book;
    }

    public void mergeBookInfo(Book book, BookRequestDto bookRequestDto, Author newAuthor) {
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(newAuthor);
        List<Genre> newGenres = bookRequestDto.getGenres().stream().map(genreMapper::mapToGenre).toList();
        book.setGenres(newGenres);
    }

    public BookResponseDto mapToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        Author author = book.getAuthor();
        AuthorResponseDto authorDto = authorMapper.mapToAuthorResponseDto(author);
        dto.setAuthor(authorDto);
        List<Genre> genres = book.getGenres();
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreDto).toList());
        return dto;
    }
}
