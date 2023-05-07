package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.GenresDao;
import ru.otus.spring.homework.oke.domain.Genre;
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
    private final GenresDao genresDao;

    private final GenreMapper genreMapper;

    @Override
    public GenreResponseDto create(GenreRequestDto genreRequestDto) {
        try {
            Genre genreForCreate = genreMapper.mapToGenre(genreRequestDto);
            Genre createdGenre = this.genresDao.create(genreForCreate);
            return genreMapper.mapToGenreResponseDto(createdGenre);
        } catch (DuplicateKeyException e) {
            String errorMessage = "Жанр с указанным именем уже существует. Укажите новое уникальное имя жанра";
            throw new NonUniqueGenreException(errorMessage, e);
        }
    }

    @Override
    public GenreResponseDto findById(long id) {
        try {
            Genre genre = this.genresDao.findById(id);
            return genreMapper.mapToGenreResponseDto(genre);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Жанр с указанным идентификатором " + id + " не найден");
        }
    }

    @Override
    public List<GenreResponseDto> findAll() {
        List<Genre> genres = this.genresDao.findAll();
        return genres.stream().map(genreMapper::mapToGenreResponseDto).collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        this.genresDao.deleteById(id);
    }
}
