package ru.otus.spring.homework.oke.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/api/book/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BookResponseDto findById(@PathVariable("id") Long id) {
        return this.bookService.findById(id);
    }

    @GetMapping(value = "/api/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BookResponseDto> findAll() {
        return this.bookService.findAll();
    }

    @PostMapping(value = "/api/book", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponseDto> create(@Valid @RequestBody BookRequestDto book) {
        BookResponseDto createdBook = this.bookService.create(book);
        return new ResponseEntity<>(createdBook, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/book/{id}")
    public HttpStatus update(@PathVariable("id") Long id, @Valid @RequestBody BookRequestDto book) {
        book.setId(id);
        this.bookService.update(book);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/api/book/{id}")
    public HttpStatus deleteById(@PathVariable("id") Long id) {
        this.bookService.deleteById(id);
        return HttpStatus.OK;
    }
}
