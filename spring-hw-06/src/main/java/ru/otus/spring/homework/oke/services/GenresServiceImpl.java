package ru.otus.spring.homework.oke.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.repositories.GenresRepository;
import ru.otus.spring.homework.oke.models.Genre;
import ru.otus.spring.homework.oke.dto.GenreRequestDto;
import ru.otus.spring.homework.oke.dto.GenreResponseDto;
import ru.otus.spring.homework.oke.exceptions.NonUniqueGenreException;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.mappers.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenresServiceImpl implements GenresService {
    private final GenresRepository genresRepository;

    private final GenreMapper genreMapper;

    @Override
    @Transactional
    public GenreResponseDto create(GenreRequestDto genreRequestDto) {
        try {
            Genre genreForCreate = genreMapper.mapToGenre(genreRequestDto);
            Genre createdGenre = this.genresRepository.save(genreForCreate);
            return genreMapper.mapToGenreResponseDto(createdGenre);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Жанр с указанным именем уже существует. Укажите новое уникальное имя жанра";
            throw new NonUniqueGenreException(errorMessage, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GenreResponseDto findById(Long id) {
        Genre genre = this.genresRepository.findById(id).orElse(null);
        if (genre == null) {
            throw new NotFoundException("Жанр с указанным идентификатором " + id + " не найден");
        }
        return genreMapper.mapToGenreResponseDto(genre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GenreResponseDto> findAll() {
        List<Genre> genres = this.genresRepository.findAll();
        return genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.genresRepository.deleteById(id);
    }
}
