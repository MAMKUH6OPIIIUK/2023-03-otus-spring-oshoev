package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Collections;
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
    @Transactional
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Author bookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        List<Genre> bookGenres = this.validateGenres(bookRequestDto.getGenreIds());
        Book bookForCreate = bookMapper.mapToBook(bookRequestDto);
        Book createdBook = this.booksDao.create(bookForCreate);
        BookResponseDto result = this.bookMapper.mapToBookResponseDto(createdBook, bookAuthor, bookGenres);
        return result;
    }

    @Override
    @Transactional
    public void update(long id, BookRequestDto bookRequestDto) {
        this.validateBook(id);
        this.validateAuthor(bookRequestDto.getAuthorId());
        this.validateGenres(bookRequestDto.getGenreIds());
        Book bookForUpdate = bookMapper.mapToBook(id, bookRequestDto);
        this.booksDao.update(bookForUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto findById(long id) {
        Book foundBook = this.validateBook(id);
        BookResponseDto result = collectSingleResponseDto(foundBook);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAll() {
        List<Book> foundBooks = this.booksDao.findAll();
        if (foundBooks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<BookResponseDto> result = collectListResponseDto(foundBooks);
        return result;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        this.booksDao.deleteById(id);
    }

    /**
     * Метод проверки существования книги
     *
     * @param id иденнификатор книги
     * @return найденная книга
     * @throws NotFoundException если книга не найдена по идентификатору
     */
    private Book validateBook(long id) {
        try {
            Book foundBook = this.booksDao.findById(id);
            return foundBook;
        } catch (Exception e) {
            throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена", e);
        }
    }

    /**
     * Метод проверки существования автора книги
     *
     * @param authorId идентификатор автора книги
     * @return найденный автор
     * @throws NotFoundException если автор по указанному идентификатору не найден
     */
    private Author validateAuthor(long authorId) {
        try {
            Author author = this.authorsDao.findById(authorId);
            return author;
        } catch (Exception e) {
            throw new NotFoundException("Автор с указанным идентификатором " + authorId + " не найден", e);
        }
    }

    /**
     * Метод проверки существования жанров книги
     *
     * @param genreIds набор идентификаторов жанров
     * @return список найденных по идентификаторам жанров, если все они найдены
     * @throws NotFoundException если набор идентификаторов жанров не пуст и хотя бы для одного из идентификаторов не
     *                           найден жанр
     */
    private List<Genre> validateGenres(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Genre> foundGenres = this.genresDao.findByIds(genreIds);
        int foundCount = foundGenres.size();
        int requiredCount = genreIds.size();
        if (foundCount < requiredCount) {
            Set<Long> foundIds = foundGenres.stream().map(Genre::getId).collect(Collectors.toSet());
            Set<Long> notFoundIds = genreIds
                    .stream()
                    .filter(genreId -> !foundIds.contains(genreId)).collect(Collectors.toSet());
            String errorMessage = "Жанры с идентификаторами: " + notFoundIds + " не найдены. Книга может иметь " +
                    "только существующие жанры";
            throw new NotFoundException(errorMessage);
        }
        return foundGenres;
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
