package ru.otus.spring.homework.oke.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.dto.AuthorResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorResponseDto create(AuthorRequestDto authorRequestDto) {
        Author authorForCreate = this.authorMapper.mapToAuthor(authorRequestDto);
        Author createdAuthor = this.authorRepository.save(authorForCreate);
        return this.authorMapper.mapToAuthorResponseDto(createdAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto findById(String id) {
        Author author = this.authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Автор с указанным идентификатором " + id + " не найден"));
        return this.authorMapper.mapToAuthorResponseDto(author);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        List<Author> authors = this.authorRepository.findAll();
        return authors.stream().map(authorMapper::mapToAuthorResponseDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        boolean isBookExists = this.bookRepository.existsByAuthorId(id);
        if (isBookExists) {
            throw new NotFoundException("Найдены книги данного автора. Сначала удалите их");
        }
        this.authorRepository.deleteById(id);
    }
}
