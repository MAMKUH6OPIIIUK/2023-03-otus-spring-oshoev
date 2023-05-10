package ru.otus.spring.homework.oke.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book mapToBook(BookRequestDto bookRequestDto) {
        Book book = new Book(bookRequestDto.getTitle(), bookRequestDto.getDescription(), bookRequestDto.getAuthorId(),
                bookRequestDto.getGenreIds());
        return book;
    }

    public Book mapToBook(long id, BookRequestDto bookRequestDto) {
        Book book = new Book(id, bookRequestDto.getTitle(), bookRequestDto.getDescription(),
                bookRequestDto.getAuthorId(), bookRequestDto.getGenreIds());
        return book;
    }

    public BookResponseDto mapToBookResponseDto(Book book, Author author, Collection<Genre> genres) {
        BookResponseDto dto = new BookResponseDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setDescription(book.getDescription());
        AuthorResponseDto authorDto = authorMapper.mapToAuthorResponseDto(author);
        dto.setAuthor(authorDto);
        dto.setGenres(genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toSet()));
        return dto;
    }
}
