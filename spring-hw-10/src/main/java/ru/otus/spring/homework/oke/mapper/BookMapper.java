package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookCreateDto bookCreateDto, Author author, Set<Genre> genres) {
        Book book = new Book();
        book.setTitle(bookCreateDto.getTitle());
        book.setDescription(bookCreateDto.getDescription());
        book.setAuthor(author);
        book.setGenres(genres);
        return book;
    }

    public void mergeBookInfo(Book book, BookUpdateDto bookRequestDto, Author newAuthor,
                              Set<Genre> newGenres) {
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(newAuthor);
        book.setGenres(newGenres);
    }

    public BookResponseDto mapToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        Author author = book.getAuthor();
        AuthorFullNameDto authorDto = authorMapper.mapToAuthorFullNameDto(author);
        dto.setAuthor(authorDto);
        Set<Genre> genres = book.getGenres();
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreResponseDto).toList());
        return dto;
    }

    public BookCreateDto mapToBookCreateDto(BookResponseDto book) {
        BookCreateDto dto = new BookCreateDto();
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        AuthorFullNameDto bookAuthor = book.getAuthor();
        dto.setAuthorId(bookAuthor.getId());
        List<GenreResponseDto> genres = book.getGenres();
        dto.setGenreIds(genres.stream().map(GenreResponseDto::getId).toList());
        return dto;
    }

    public BookUpdateDto mapToBookUpdateDto(BookResponseDto book) {
        BookUpdateDto dto = new BookUpdateDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        AuthorFullNameDto bookAuthor = book.getAuthor();
        dto.setAuthorId(bookAuthor.getId());
        List<GenreResponseDto> genres = book.getGenres();
        dto.setGenreIds(genres.stream().map(GenreResponseDto::getId).toList());
        return dto;
    }
}
