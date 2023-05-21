package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.homework.oke.model.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с авторами должен ")
@DataJpaTest()
@Import({JpaAuthorRepository.class})
public class JpaAuthorRepositoryTest {
    private static final Long FIRST_AUTHOR_ID = 1L;
    private static final Long SECOND_AUTHOR_ID = 2L;
    private static final Long THIRD_AUTHOR_ID = 3L;

    private static final Long FIRST_NON_EXISTING_AUTHOR_ID = 4L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private JpaAuthorRepository authorsRepository;

    @DisplayName("добавлять автора в БД")
    @Test
    void shouldInsertAuthor() {
        Author author = em.find(Author.class, FIRST_NON_EXISTING_AUTHOR_ID);
        assertThat(author).isNull();

        Author authorForSave = new Author("Иван", null, "Иванович", "Иванов");
        this.authorsRepository.save(authorForSave);

        author = em.find(Author.class, FIRST_NON_EXISTING_AUTHOR_ID);
        assertThat(author).usingRecursiveComparison().isEqualTo(authorForSave);
    }

    @DisplayName("возвращать ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = em.find(Author.class, THIRD_AUTHOR_ID);
        Optional<Author> actualAuthor = this.authorsRepository.findById(THIRD_AUTHOR_ID);
        assertThat(actualAuthor).isPresent().get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName("возвращать ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorList() {
        Author expectedAuthor1 = em.find(Author.class, FIRST_AUTHOR_ID);
        Author expectedAuthor2 = em.find(Author.class, SECOND_AUTHOR_ID);
        Author expectedAuthor3 = em.find(Author.class, THIRD_AUTHOR_ID);
        List<Author> actualAuthors = this.authorsRepository.findAll();
        assertThat(actualAuthors)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedAuthor1, expectedAuthor2, expectedAuthor3);
    }

    @DisplayName("удалять заданного автора по его id")
    @Test
    void shouldCorrectDeleteAuthorById() {
        int expectedAuthorCount = 2;
        Author author = em.find(Author.class, THIRD_AUTHOR_ID);
        assertThat(author).isNotNull();

        this.authorsRepository.deleteById(THIRD_AUTHOR_ID);

        int actualAuthorCount = this.authorsRepository.findAll().size();
        author = em.find(Author.class, THIRD_AUTHOR_ID);
        assertThat(author).isNull();
        assertThat(actualAuthorCount).isEqualTo(expectedAuthorCount);
    }
}
