package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами должен ")
@DataJpaTest
public class BookRepositoryTest {
    private static final Long FIRST_NON_EXISTING_BOOK_ID = 4L;

    private static final Long FIRST_EXISTING_BOOK_ID = 1L;

    private static final Long SECOND_EXISTING_BOOK_ID = 2L;

    private static final Long THIRD_EXISTING_BOOK_ID = 3L;

    private static final Long FIRST_EXISTING_AUTHOR_ID = 1L;

    private static final Long SECOND_EXISTING_AUTHOR_ID = 2L;

    private static final Long FIRST_EXISTING_GENRE_ID = 1L;

    private static final Long SECOND_EXISTING_GENRE_ID = 3L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("добавлять книгу с привязкой к автору и жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        Author bookAuthor = em.find(Author.class, FIRST_EXISTING_AUTHOR_ID);
        Genre bookGenre = em.find(Genre.class, FIRST_EXISTING_GENRE_ID);

        Book expectedBook = new Book("Книга4", "Описание4", bookAuthor, Set.of(bookGenre));
        this.bookRepository.save(expectedBook);
        Book actualBook = em.find(Book.class, FIRST_NON_EXISTING_BOOK_ID);
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("обновлять книгу и её привязки к жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateBook() {
        Book book = em.find(Book.class, THIRD_EXISTING_BOOK_ID);

        Author newAuthor = em.find(Author.class, FIRST_EXISTING_AUTHOR_ID);
        Genre newGenre = em.find(Genre.class, SECOND_EXISTING_GENRE_ID);
        Set<Genre> newGenres = new HashSet<>();
        newGenres.add(newGenre);
        Book bookForUpdate = book;
        bookForUpdate.setAuthor(newAuthor);
        bookForUpdate.setGenres(newGenres);

        this.bookRepository.save(bookForUpdate);

        book = em.find(Book.class, THIRD_EXISTING_BOOK_ID);
        assertThat(book).usingRecursiveComparison().isEqualTo(bookForUpdate);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        Optional<Book> actualBook = this.bookRepository.findById(FIRST_EXISTING_BOOK_ID);
        Book expectedBook = em.find(Book.class, FIRST_EXISTING_BOOK_ID);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг по идентификатору автора")
    @Test
    void shouldReturnExpectedBookListByAuthorId() {
        List<Book> actualBooks = this.bookRepository.findByAuthorId(SECOND_EXISTING_AUTHOR_ID);
        Book expectedBook = em.find(Book.class, THIRD_EXISTING_BOOK_ID);
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(expectedBook);
    }

    @DisplayName("возвращать ожидаемый всех список книг")
    @Test
    void shouldReturnExpectedBookList() {
        List<Book> actualBooks = this.bookRepository.findAll();
        Book expectedBook1 = em.find(Book.class, FIRST_EXISTING_BOOK_ID);
        Book expectedBook2 = em.find(Book.class, SECOND_EXISTING_BOOK_ID);
        Book expectedBook3 = em.find(Book.class, THIRD_EXISTING_BOOK_ID);
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedBook1, expectedBook2, expectedBook3);
    }

    @DisplayName("удалять книгу по её идентификатору")
    @Test
    void shouldCorrectDeleteBookById() {
        int expectedBookCount = 2;
        Book book = em.find(Book.class, FIRST_EXISTING_BOOK_ID);

        this.bookRepository.deleteById(FIRST_EXISTING_BOOK_ID);

        int actualBookCount = this.bookRepository.findAll().size();
        book = em.find(Book.class, FIRST_EXISTING_BOOK_ID);
        assertThat(book).isNull();
        assertThat(actualBookCount).isEqualTo(expectedBookCount);

    }
}
