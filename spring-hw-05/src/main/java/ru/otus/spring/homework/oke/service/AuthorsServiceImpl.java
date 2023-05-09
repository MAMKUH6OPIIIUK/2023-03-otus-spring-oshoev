package ru.otus.spring.homework.oke.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dao.AuthorsDao;
import ru.otus.spring.homework.oke.dao.BooksDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
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

    private final BooksDao booksDao;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponseDto create(AuthorRequestDto authorRequestDto) {
        Author authorForCreate = this.authorMapper.mapToAuthor(authorRequestDto);
        Author createdAuthor = this.authorsDao.create(authorForCreate);
        return this.authorMapper.mapToAuthorResponseDto(createdAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto findById(long id) {
        try {
            Author author = this.authorsDao.findById(id);
            return this.authorMapper.mapToAuthorResponseDto(author);
        } catch (Exception e) {
            throw new NotFoundException("Автор с указанным идентификатором " + id + " не найден");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        List<Author> authors = this.authorsDao.findAll();
        return authors.stream().map(authorMapper::mapToAuthorResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        List<Book> authorBooks = this.booksDao.findByAuthorId(id);
        if (authorBooks.size() != 0) {
            throw new AuthorBooksFoundException("Найдены книги данного автора. Сначала удалите их");
        }
        this.authorsDao.deleteById(id);
    }
}
