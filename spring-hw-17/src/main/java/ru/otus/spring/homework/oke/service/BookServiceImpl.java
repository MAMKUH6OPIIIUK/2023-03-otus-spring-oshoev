package ru.otus.spring.homework.oke.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.GenreRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Author bookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        Set<Genre> bookGenres = this.validateGenres(bookRequestDto.getGenreIds());
        Book bookForCreate = bookMapper.mapToBook(bookRequestDto, bookAuthor, bookGenres);
        Book createdBook = this.bookRepository.save(bookForCreate);
        BookResponseDto result = this.bookMapper.mapToBookResponseDto(createdBook);
        return result;
    }

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public void update(BookRequestDto bookRequestDto) {
        Book bookForUpdate = this.validateBook(bookRequestDto.getId());
        Author newBookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        Set<Genre> newBookGenres = this.validateGenres(bookRequestDto.getGenreIds());
        this.bookMapper.mergeBookInfo(bookForUpdate, bookRequestDto, newBookAuthor, newBookGenres);
        this.bookRepository.save(bookForUpdate);
    }

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional(readOnly = true)
    public BookResponseDto findById(Long id) {
        Book foundBook = this.validateBook(id);
        BookResponseDto result = bookMapper.mapToBookResponseDto(foundBook);
        return result;
    }

    @Override
    @CircuitBreaker(name = "long-running-business-method")
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAll() {
        List<Book> foundBooks = this.bookRepository.findAll();
        return foundBooks
                .stream()
                .map(bookMapper::mapToBookResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public void deleteById(Long id) {
        this.bookRepository.deleteById(id);
    }

    /**
     * Метод проверки существования книги
     *
     * @param id иденнификатор книги
     * @return найденная книга
     * @throws NotFoundException если книга не найдена по идентификатору
     */
    private Book validateBook(Long id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Книга с указанным идентификатором " + id + " не найдена"));
        return book;
    }

    /**
     * Метод проверки существования автора книги
     *
     * @param authorId идентификатор автора книги
     * @return найденный автор
     * @throws NotFoundException если автор по указанному идентификатору не найден
     */
    private Author validateAuthor(Long authorId) {
        Author author = this.authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Автор с указанным идентификатором " + authorId +
                        " не найден"));
        return author;
    }

    /**
     * Метод проверки существования жанров книги
     *
     * @param genreIds набор идентификаторов жанров
     * @return набор найденных по идентификаторам жанров, если все они найдены
     * @throws NotFoundException если набор идентификаторов жанров не пуст и хотя бы для одного из идентификаторов не
     *                           найден жанр
     */
    private Set<Genre> validateGenres(List<Long> genreIds) {
        Set<Genre> foundGenres = this.genreRepository.findByIdIn(genreIds);
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
}
