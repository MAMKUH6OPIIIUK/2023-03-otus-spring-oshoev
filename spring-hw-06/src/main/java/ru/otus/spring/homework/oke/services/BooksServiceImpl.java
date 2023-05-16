package ru.otus.spring.homework.oke.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.models.Author;
import ru.otus.spring.homework.oke.models.Book;
import ru.otus.spring.homework.oke.models.Genre;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.mappers.BookMapper;
import ru.otus.spring.homework.oke.repositories.AuthorsRepository;
import ru.otus.spring.homework.oke.repositories.BooksRepository;
import ru.otus.spring.homework.oke.repositories.GenresRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {
    private final BooksRepository booksRepository;

    private final AuthorsRepository authorsRepository;

    private final GenresRepository genresRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Author bookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        List<Genre> bookGenres = this.validateGenres(bookRequestDto.getGenreIds());
        Book bookForCreate = bookMapper.mapToBook(bookRequestDto, bookAuthor, bookGenres);
        Book createdBook = this.booksRepository.save(bookForCreate);
        BookResponseDto result = this.bookMapper.mapToBookResponseDto(createdBook);
        return result;
    }

    @Override
    @Transactional
    public void update(Long id, BookRequestDto bookRequestDto) {
        this.validateBook(id);
        Author bookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        List<Genre> bookGenres = this.validateGenres(bookRequestDto.getGenreIds());
        Book bookForUpdate = bookMapper.mapToBook(id, bookRequestDto, bookAuthor, bookGenres);
        this.booksRepository.save(bookForUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto findById(Long id) {
        Book foundBook = this.validateBook(id);
        BookResponseDto result = bookMapper.mapToBookResponseDto(foundBook);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAll() {
        List<Book> foundBooks = this.booksRepository.findAll();
        if (foundBooks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<BookResponseDto> result = foundBooks
                .stream()
                .map(bookMapper::mapToBookResponseDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.booksRepository.deleteById(id);
    }

    /**
     * Метод проверки существования книги
     *
     * @param id иденнификатор книги
     * @return найденная книга
     * @throws NotFoundException если книга не найдена по идентификатору
     */
    private Book validateBook(Long id) {
        Book book = this.booksRepository.findById(id).orElse(null);
        if (book == null) {
            throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена");
        }
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
        Author author = this.authorsRepository.findById(authorId).orElse(null);
        if (author == null) {
            throw new NotFoundException("Автор с указанным идентификатором " + authorId + " не найден");
        }
        return author;
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
        List<Genre> foundGenres = this.genresRepository.findByIds(genreIds);
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
