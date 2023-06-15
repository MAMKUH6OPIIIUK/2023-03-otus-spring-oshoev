package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.repository.GenreRepository;
import ru.otus.spring.homework.oke.model.Genre;
import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional
    public GenreResponseDto create(GenreRequestDto genreRequestDto) {
        try {
            Genre genreForCreate = genreMapper.mapToGenre(genreRequestDto);
            Genre createdGenre = this.genreRepository.save(genreForCreate);
            return genreMapper.mapToGenreResponseDto(createdGenre);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Жанр с указанным именем уже существует. Укажите новое уникальное имя жанра";
            throw new NotFoundException(errorMessage, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponseDto findById(Long id) {
        Genre genre = this.genreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с указанным идентификатором " + id + " не найден"));
        return genreMapper.mapToGenreResponseDto(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponseDto> findAll() {
        List<Genre> genres = this.genreRepository.findAll();
        return genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.genreRepository.deleteById(id);
    }
}
