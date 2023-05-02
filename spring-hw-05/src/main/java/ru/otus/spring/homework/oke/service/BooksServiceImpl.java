package ru.otus.spring.homework.oke.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.homework.oke.dao.BooksDao;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {
    private final BooksDao booksDao;


    @Override
    public Book create(String title, String description, long authorId, long[] genreIds) {
        Set<Genre> genreIdsForBook = new HashSet<>();
        for (long genreId : genreIds) {
            genreIdsForBook.add(new Genre(genreId, null));
        }
        Author bookAuthor = new Author(authorId, null, null, null, null);
        Book bookToCreate = new Book(0, title, description, bookAuthor, genreIdsForBook);
        return this.booksDao.create(bookToCreate);
    }

    @Override
    public Book update(long id, String title, String description, long authorId, long[] genreIds) {
        Set<Genre> genreIdsForBook = new HashSet<>();
        for (long genreId : genreIds) {
            genreIdsForBook.add(new Genre(genreId, null));
        }
        Author bookAuthor = new Author(authorId, null, null, null, null);
        Book bookToUpdate = new Book(id, title, description, bookAuthor, genreIdsForBook);
        return this.booksDao.update(bookToUpdate);
    }

    @Override
    public Book findById(long id) {
        return this.booksDao.findById(id);
    }

    @Override
    public List<Book> findAll() {
        return this.booksDao.findAll();
    }

    @Override
    public void deleteById(long id) {
        this.booksDao.deleteById(id);
    }
}
