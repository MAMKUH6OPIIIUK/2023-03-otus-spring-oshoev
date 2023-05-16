package ru.otus.spring.homework.oke.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.repositories.AuthorsRepository;
import ru.otus.spring.homework.oke.repositories.BooksRepository;
import ru.otus.spring.homework.oke.models.Author;
import ru.otus.spring.homework.oke.models.Book;
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
    private final AuthorsRepository authorsRepository;

    private final BooksRepository booksRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponseDto create(AuthorRequestDto authorRequestDto) {
        Author authorForCreate = this.authorMapper.mapToAuthor(authorRequestDto);
        Author createdAuthor = this.authorsRepository.save(authorForCreate);
        return this.authorMapper.mapToAuthorResponseDto(createdAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto findById(Long id) {
        Author author = this.authorsRepository.findById(id).orElse(null);
        if (author == null) {
            throw new NotFoundException("Автор с указанным идентификатором " + id + " не найден");
        }
        return this.authorMapper.mapToAuthorResponseDto(author);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        List<Author> authors = this.authorsRepository.findAll();
        return authors.stream().map(authorMapper::mapToAuthorResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        List<Book> authorBooks = this.booksRepository.findByAuthorId(id);
        if (authorBooks.size() != 0) {
            throw new AuthorBooksFoundException("Найдены книги данного автора. Сначала удалите их");
        }
        this.authorsRepository.deleteById(id);
    }
}
