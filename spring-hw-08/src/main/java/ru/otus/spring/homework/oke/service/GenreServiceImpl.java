package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.GenreDto;
import ru.otus.spring.homework.oke.mapper.GenreMapper;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.repository.GenreRepositoryCustom;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepositoryCustom genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDto> findAll() {
        List<Genre> genres = this.genreRepository.findAll();
        return genres.stream().map(genreMapper::mapToGenreDto).collect(Collectors.toList());
    }

}
