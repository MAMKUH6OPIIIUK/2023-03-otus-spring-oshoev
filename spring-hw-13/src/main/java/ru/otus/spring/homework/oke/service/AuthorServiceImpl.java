package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.AuthorFullNameDto;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.dto.AuthorRequestDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.AuthorMapper;

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
    public AuthorFullNameDto create(AuthorRequestDto authorRequestDto) {
        Author authorForCreate = this.authorMapper.mapToAuthor(authorRequestDto);
        Author createdAuthor = this.authorRepository.save(authorForCreate);
        return this.authorMapper.mapToAuthorFullNameDto(createdAuthor);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorFullNameDto findById(Long id) {
        Author author = this.authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Автор с указанным идентификатором " + id + " не найден"));
        return this.authorMapper.mapToAuthorFullNameDto(author);

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorFullNameDto> findAll() {
        List<Author> authors = this.authorRepository.findAll();
        return authors.stream().map(authorMapper::mapToAuthorFullNameDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        long authorBooksCount = this.bookRepository.countByAuthorId(id);
        if (authorBooksCount != 0) {
            throw new NotFoundException("Найдены книги данного автора. Сначала удалите их");
        }
        this.authorRepository.deleteById(id);
    }
}
