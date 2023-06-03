package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.BookRequestDto;
import ru.otus.spring.homework.oke.dto.BookResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.BookMapper;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.repository.AuthorRepository;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final CommentRepository commentRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookResponseDto create(BookRequestDto bookRequestDto) {
        Author bookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        Book bookForCreate = bookMapper.mapToBook(bookRequestDto, bookAuthor);
        Book createdBook = this.bookRepository.save(bookForCreate);
        BookResponseDto result = this.bookMapper.mapToBookResponseDto(createdBook);
        return result;
    }

    @Override
    @Transactional
    public void update(BookRequestDto bookRequestDto) {
        Book bookForUpdate = this.validateBook(bookRequestDto.getId());
        Author newBookAuthor = this.validateAuthor(bookRequestDto.getAuthorId());
        this.bookMapper.mergeBookInfo(bookForUpdate, bookRequestDto, newBookAuthor);
        this.bookRepository.save(bookForUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto findById(String id) {
        Book foundBook = this.validateBook(id);
        BookResponseDto result = bookMapper.mapToBookResponseDto(foundBook);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAll() {
        List<Book> foundBooks = this.bookRepository.findAll();
        if (foundBooks.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<BookResponseDto> result = foundBooks
                .stream()
                .map(bookMapper::mapToBookResponseDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        this.commentRepository.deleteByBookId(id);
        this.bookRepository.deleteById(id);
    }

    /**
     * Метод проверки существования книги
     *
     * @param id иденнификатор книги
     * @return найденная книга
     * @throws NotFoundException если книга не найдена по идентификатору
     */
    private Book validateBook(String id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Книга с указанным идентификатором " + id + " не найдена"));
        return book;
    }

    /**
     * Метод проверки существования автора книги
     *
     * @param authorId идентификатор автора книги
     * @return найденный автор
     * @throws NotFoundException если автор по указанному идентификатору не найден
     */
    private Author validateAuthor(String authorId) {
        Author author = this.authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Автор с указанным идентификатором " + authorId +
                        " не найден"));
        return author;
    }
}
