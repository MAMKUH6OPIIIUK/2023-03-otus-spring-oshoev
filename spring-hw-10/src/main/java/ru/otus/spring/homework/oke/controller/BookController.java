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
import ru.otus.spring.homework.oke.dto.BookCreateDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.dto.BookUpdateDto;
import ru.otus.spring.homework.oke.dto.validation.UpdateBookFullScope;
import ru.otus.spring.homework.oke.service.BookService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/{id}")
    public BookResponseDto findById(@PathVariable("id") Long id) {
        return this.bookService.findById(id);
    }

    @GetMapping
    public List<BookResponseDto> findAll() {
        return this.bookService.findAll();
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public BookResponseDto create(@Validated @RequestBody BookCreateDto book) {
        return this.bookService.create(book);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable("id") Long id,
                       @Validated(UpdateBookFullScope.class) @RequestBody BookUpdateDto book) {
        book.setId(id);
        this.bookService.update(book);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.bookService.deleteById(id);
    }
}
