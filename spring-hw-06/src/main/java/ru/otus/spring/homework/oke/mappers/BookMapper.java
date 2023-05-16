package ru.otus.spring.homework.oke.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.models.Author;
import ru.otus.spring.homework.oke.models.Book;
import ru.otus.spring.homework.oke.models.Genre;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookRequestDto bookRequestDto, Author author, Collection<Genre> genres) {
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(author);
        book.setGenres(genres.stream().collect(Collectors.toSet()));
        return book;
    }

    public Book mapToBook(Long id, BookRequestDto bookRequestDto, Author author, Collection<Genre> genres) {
        Book book = mapToBook(bookRequestDto, author, genres);
        book.setId(id);
        return book;
    }

    public BookResponseDto mapToBookResponseDto(Book book) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        Author author = book.getAuthor();
        AuthorResponseDto authorDto = authorMapper.mapToAuthorResponseDto(author);
        dto.setAuthor(authorDto);
        Set<Genre> genres = book.getGenres();
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toSet()));
        return dto;
    }
}
