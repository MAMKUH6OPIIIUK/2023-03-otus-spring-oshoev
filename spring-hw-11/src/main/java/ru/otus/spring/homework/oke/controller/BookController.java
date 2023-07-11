package ru.otus.spring.homework.oke.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;
import ru.otus.spring.homework.oke.dto.validation.UpdateBookFullScope;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

@RestController
@RequestMapping(value = "/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    @GetMapping(value = "/{id}")
    public Mono<BookResponseDto> findById(@PathVariable("id") String id) {
        return this.bookRepository.findById(id)
                .map(bookMapper::mapToBookResponseDto)
                .switchIfEmpty(Mono.fromCallable(() -> {
                    throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена");
                }));
    }

    @GetMapping
    public Flux<BookResponseDto> findAll() {
        return bookRepository.findAll()
                .map(bookMapper::mapToBookResponseDto);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Mono<BookResponseDto> create(@Validated @RequestBody BookCreateDto book) {
        return this.authorRepository.findById(book.getAuthorId())
                .switchIfEmpty(Mono.fromCallable(() -> {
                    throw new NotFoundException("Автор с указанным идентификатором " + book.getAuthorId()
                            + " не найден");
                }))
                .map(author -> bookMapper.mapToBook(book, author))
                .flatMap(bookRepository::save)
                .map(bookMapper::mapToBookResponseDto);
    }

    @PutMapping(value = "/{id}")
    public Mono<Void> update(@PathVariable("id") String id,
                             @Validated(UpdateBookFullScope.class) @RequestBody BookUpdateDto book) {
        book.setId(id);
        return this.bookRepository.findById(id)
                .switchIfEmpty(Mono.fromCallable(() -> {
                    throw new NotFoundException("Книга с указанным идентификатором " + id + " не найдена");
                }))
                .flatMap(bookForUpdate -> authorRepository.findById(book.getAuthorId())
                        .switchIfEmpty(Mono.fromCallable(() -> {
                            throw new NotFoundException("Автор с указанным идентификатором " + book.getAuthorId()
                                    + " не найден");
                        }))
                        .map(newAuthor -> bookMapper.mergeBookInfo(bookForUpdate, book, newAuthor))
                        .flatMap(bookRepository::save))
                .then();
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return this.commentRepository.deleteByBookId(id)
                .then(this.bookRepository.deleteById(id));
    }
}
