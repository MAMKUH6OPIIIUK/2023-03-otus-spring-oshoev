package ru.otus.spring.homework.oke.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.repository.AuthorRepository;

@RestController
@RequestMapping(value = "/api/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @GetMapping
    public Flux<AuthorFullNameDto> findAll() {
        return this.authorRepository.findAll()
                .map(authorMapper::mapToAuthorFullNameDto);
    }
}
