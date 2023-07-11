package ru.otus.spring.homework.oke.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.spring.homework.oke.dto.GenreDto;
import ru.otus.spring.homework.oke.mapper.GenreMapper;
import ru.otus.spring.homework.oke.repository.GenreRepositoryCustom;

@RestController
@RequestMapping(value = "/api/genre")
@RequiredArgsConstructor
public class GenreController {
    private final GenreRepositoryCustom genreRepository;

    private final GenreMapper genreMapper;

    @GetMapping
    public Flux<GenreDto> findAll() {
        return this.genreRepository.findAll()
                .map(genreMapper::mapToGenreDto);
    }
}
