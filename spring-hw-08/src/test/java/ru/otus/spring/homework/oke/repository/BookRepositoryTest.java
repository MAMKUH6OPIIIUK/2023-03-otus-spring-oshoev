package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.changelog.test.data.DataGenerator;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с книгами должен ")
@DataMongoTest
public class BookRepositoryTest {
    private static final String NON_EXISTING_BOOK_ID = "non_existing_book";

    private static final String FIRST_EXISTING_BOOK_ID = "1";

    private static final String THIRD_EXISTING_BOOK_ID = "3";


    private static final String SECOND_EXISTING_AUTHOR_ID = "2";

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("добавлять книгу с привязкой к автору и жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldInsertBook() {
        Author bookAuthor = DataGenerator.getFirstAuthor();
        Genre bookGenre = new Genre("Жанр4");

        Book expectedBook = new Book(NON_EXISTING_BOOK_ID, "Книга4", "Описание4", bookAuthor, List.of(bookGenre));
        this.bookRepository.save(expectedBook);
        Optional<Book> actualBook = this.bookRepository.findById(NON_EXISTING_BOOK_ID);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("обновлять книгу и её привязки к жанрам в БД")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void shouldUpdateBook() {
        Book book = DataGenerator.getThirdBook();

        Author newAuthor = DataGenerator.getFirstAuthor();
        Genre newGenre = new Genre("Жанр2");
        List<Genre> newGenres = new ArrayList<>();
        newGenres.add(newGenre);
        Book bookForUpdate = book;
        bookForUpdate.setAuthor(newAuthor);
        bookForUpdate.setGenres(newGenres);

        this.bookRepository.save(bookForUpdate);

        Optional<Book> actualBook = this.bookRepository.findById(THIRD_EXISTING_BOOK_ID);
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(bookForUpdate);
    }

    @DisplayName("возвращать ожидаемую книгу по её id")
    @Test
    void shouldReturnExpectedBookById() {
        Optional<Book> actualBook = this.bookRepository.findById(THIRD_EXISTING_BOOK_ID);
        Book expectedBook = DataGenerator.getThirdBook();
        assertThat(actualBook).isPresent().get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список книг по идентификатору автора")
    @Test
    void shouldReturnExpectedBookListByAuthorId() {
        List<Book> actualBooks = this.bookRepository.findByAuthorId(SECOND_EXISTING_AUTHOR_ID);
        Book expectedBook = DataGenerator.getThirdBook();
        assertThat(actualBooks).usingRecursiveFieldByFieldElementComparator().containsExactlyInAnyOrder(expectedBook);
    }

    @DisplayName("возвращать ожидаемый список всех книг")
    @Test
    void shouldReturnExpectedBookList() {
        List<Book> actualBooks = this.bookRepository.findAll();
        Book expectedBook1 = DataGenerator.getFirstBook();
        Book expectedBook2 = DataGenerator.getSecondBook();
        Book expectedBook3 = DataGenerator.getThirdBook();
        assertThat(actualBooks)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedBook1, expectedBook2, expectedBook3);
    }

    @DisplayName("удалять книгу по её идентификатору")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCorrectDeleteBookById() {
        int expectedBookCount = 2;
        Optional<Book> book = this.bookRepository.findById(FIRST_EXISTING_BOOK_ID);
        assertThat(book).isPresent();

        this.bookRepository.deleteById(FIRST_EXISTING_BOOK_ID);

        int actualBookCount = this.bookRepository.findAll().size();
        book = this.bookRepository.findById(FIRST_EXISTING_BOOK_ID);
        assertThat(book).isNotPresent();
        assertThat(actualBookCount).isEqualTo(expectedBookCount);
    }
}
