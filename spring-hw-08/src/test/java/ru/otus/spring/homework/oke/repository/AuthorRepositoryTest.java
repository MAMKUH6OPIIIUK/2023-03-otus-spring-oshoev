package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.changelog.test.data.DataGenerator;
import ru.otus.spring.homework.oke.model.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами должен ")
@DataMongoTest
public class AuthorRepositoryTest {
    private static final String THIRD_AUTHOR_ID = "3";

    private static final String NON_EXISTING_AUTHOR_ID = "non_existing_author";

    @Autowired
    private AuthorRepository authorRepository;

    @DisplayName("добавлять автора в БД")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertAuthor() {
        Author author = new Author(NON_EXISTING_AUTHOR_ID, "Иван", null, "Иванович", "Иванов");
        this.authorRepository.save(author);
        Optional<Author> actualAuthor = this.authorRepository.findById(NON_EXISTING_AUTHOR_ID);
        assertThat(actualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(author);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = DataGenerator.getThirdAuthor();
        Optional<Author> actualAuthor = this.authorRepository.findById(THIRD_AUTHOR_ID);
        assertThat(actualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorList() {
        Author expectedAuthor1 = DataGenerator.getFirstAuthor();
        Author expectedAuthor2 = DataGenerator.getSecondAuthor();
        Author expectedAuthor3 = DataGenerator.getThirdAuthor();
        List<Author> actualAuthors = this.authorRepository.findAll();
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2, expectedAuthor3);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCorrectDeleteAuthorById() {
        int expectedAuthorCount = 2;
        Optional<Author> author = this.authorRepository.findById(THIRD_AUTHOR_ID);
        assertThat(author).isPresent();

        this.authorRepository.deleteById(THIRD_AUTHOR_ID);

        int actualAuthorCount = this.authorRepository.findAll().size();
        author = this.authorRepository.findById(THIRD_AUTHOR_ID);
        assertThat(author).isNotPresent();
        assertThat(actualAuthorCount).isEqualTo(expectedAuthorCount);
    }
}
