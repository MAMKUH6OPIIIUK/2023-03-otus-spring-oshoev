package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.AuthorsDao;
import ru.otus.spring.homework.oke.dao.BooksDao;
import ru.otus.spring.homework.oke.dao.GenresDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.mappers.BookMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {
    private final BooksDao booksDao;

    private final AuthorsDao authorsDao;

    private final GenresDao genresDao;

    private final BookMapper bookMapper;

    @Override
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        try {
            Book bookForCreate = bookMapper.mapToBook(bookRequestDto);
            Book createdBook = this.booksDao.create(bookForCreate);
            BookResponseDto result = collectSingleResponseDto(createdBook);
            return result;
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Указанный жанр или автор не существует. Книга может иметь только существующие " +
                    "жанры и автора";
            throw new NotFoundException(errorMessage, e);
        }
    }

    @Override
    public BookResponseDto update(long id, BookRequestDto bookRequestDto) {
        try {
            Book bookForUpdate = bookMapper.mapToBook(id, bookRequestDto);
            Book updatedBook = this.booksDao.update(bookForUpdate);
            BookResponseDto result = collectSingleResponseDto(updatedBook);
            return result;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена");
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Указанный жанр или автор не существует. Книга может иметь только существующие " +
                    "жанры и автора";
            throw new NotFoundException(errorMessage, e);
        }
    }

    @Override
    public BookResponseDto findById(long id) {
        try {
            Book foundBook = this.booksDao.findById(id);
            BookResponseDto result = collectSingleResponseDto(foundBook);
            return result;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена");
        }
    }

    @Override
    public List<BookResponseDto> findAll() {
        List<Book> foundBooks = this.booksDao.findAll();
        if (foundBooks.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<BookResponseDto> result = collectListResponseDto(foundBooks);
            return result;
        }
    }

    @Override
    public void deleteById(long id) {
        this.booksDao.deleteById(id);
    }

    private BookResponseDto collectSingleResponseDto(Book book) {
        long bookId = book.getId();
        long authorId = book.getAuthorId();
        Author author = this.authorsDao.findById(authorId);
        List<Genre> genres = this.genresDao.findAllByBookId(bookId);
        BookResponseDto result = this.bookMapper.mapToBookResponseDto(book, author, genres);
        return result;
    }

    private List<BookResponseDto> collectListResponseDto(List<Book> books) {
        List<Genre> genres = genresDao.findAllUsed();
        List<Author> authors = authorsDao.findAll();
        Map<Long, Author> authorsMap = authors.stream().collect(Collectors.toMap(Author::getId, Function.identity()));
        Map<Long, Genre> genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        List<BookResponseDto> result = new ArrayList<>();
        for (Book book : books) {
            Author bookAuthor = authorsMap.get(book.getAuthorId());
            Set<Genre> bookGenres = new HashSet<>();
            for (Long genreId : book.getGenreIds()) {
                if (genresMap.containsKey(genreId)) {
                    bookGenres.add(genresMap.get(genreId));
                }
            }
            BookResponseDto dto = bookMapper.mapToBookResponseDto(book, bookAuthor, bookGenres);
            result.add(dto);
        }
        return result;
    }
}
