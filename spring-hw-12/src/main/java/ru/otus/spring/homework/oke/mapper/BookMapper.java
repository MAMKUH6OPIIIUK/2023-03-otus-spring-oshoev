package ru.otus.spring.homework.oke.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
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

    public Book mapToBook(BookRequestDto bookRequestDto, Author author, Set<Genre> genres) {
        Book book = new Book();
        book.setId(bookRequestDto.getId());
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(author);
        book.setGenres(genres);
        return book;
    }

    public void mergeBookInfo(Book book, BookRequestDto bookRequestDto, Author newAuthor,
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

    public BookRequestDto mapToBookRequestDto(BookResponseDto book) {
        BookRequestDto dto = new BookRequestDto();
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
