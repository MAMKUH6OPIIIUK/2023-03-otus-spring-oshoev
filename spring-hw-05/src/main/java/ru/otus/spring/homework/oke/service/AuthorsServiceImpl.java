package ru.otus.spring.homework.oke.service;


import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.AuthorsDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.exceptions.AuthorBooksFoundException;
import ru.otus.spring.homework.oke.exceptions.NotFoundException;
import ru.otus.spring.homework.oke.mappers.AuthorMapper;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorsServiceImpl implements AuthorsService {
    private final AuthorsDao authorsDao;

    private final AuthorMapper authorMapper;

    @Override
    public AuthorResponseDto create(AuthorRequestDto authorRequestDto) {
        Author authorForCreate = this.authorMapper.mapToAuthor(authorRequestDto);
        Author createdAuthor = this.authorsDao.create(authorForCreate);
        return this.authorMapper.mapToAuthorResponseDto(createdAuthor);
    }

    @Override
    public AuthorResponseDto findById(long id) {
        try {
            Author author = this.authorsDao.findById(id);
            return this.authorMapper.mapToAuthorResponseDto(author);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Автор с указанным идентификатором " + id + " не найден");
        }
    }

    @Override
    public List<AuthorResponseDto> findAll() {
        List<Author> authors = this.authorsDao.findAll();
        return authors.stream().map(authorMapper::mapToAuthorResponseDto).collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        try {
            this.authorsDao.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Найдены книги данного автора. Сначала удалите их";
            throw new AuthorBooksFoundException(errorMessage, e);
        }
    }
}
