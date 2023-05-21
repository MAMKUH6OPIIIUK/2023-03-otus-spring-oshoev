package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий для работы с комментариями должен ")
@DataJpaTest
@Import(JpaCommentRepository.class)
public class JpaCommentRepositoryTest {
    private static final Long EXISTING_BOOK_ID = 1L;

    private static final Long FIRST_NON_EXISTING_COMMENT_ID = 4L;

    private static final Long FIRST_EXISTING_COMMENT_ID = 1L;

    private static final Long SECOND_EXISTING_COMMENT_ID = 2L;

    private static final Long THIRD_EXISTING_COMMENT_ID = 3L;

    @Autowired
    private TestEntityManager em;

    @Autowired
    private JpaCommentRepository commentsRepository;

    @DisplayName("добавлять комментарий к книге в БД")
    @Test
    void shouldInsertComment() {
        Comment comment = em.find(Comment.class, FIRST_NON_EXISTING_COMMENT_ID);
        assertThat(comment).isNull();

        Book book = em.find(Book.class, EXISTING_BOOK_ID);
        Comment commentForSave = new Comment("Комментарий", book);
        this.commentsRepository.save(commentForSave);

        comment = em.find(Comment.class, FIRST_NON_EXISTING_COMMENT_ID);
        assertThat(comment).usingRecursiveComparison().isEqualTo(commentForSave);
    }

    @DisplayName("обновлять комментарий к книге в БД")
    @Test
    void  shouldUpdateComment() {
        Comment comment = em.find(Comment.class, THIRD_EXISTING_COMMENT_ID);

        Book book = em.find(Book.class, EXISTING_BOOK_ID);
        Comment commentForUpdate = comment;
        commentForUpdate.setBook(book);
        commentForUpdate.setText("Новый текст");
        this.commentsRepository.save(commentForUpdate);

        comment = em.find(Comment.class, THIRD_EXISTING_COMMENT_ID);
        System.out.println(comment.getText());
        assertThat(comment).usingRecursiveComparison().isEqualTo(commentForUpdate);
    }

    @DisplayName("возвращать ожидаемый комментарий по его id")
    @Test
    void shouldReturnExpectedCommentById() {
        Optional<Comment> actualOptionalComment = this.commentsRepository.findById(FIRST_EXISTING_COMMENT_ID);
        Comment expectedComment = em.find(Comment.class, FIRST_EXISTING_COMMENT_ID);
        assertThat(actualOptionalComment).isPresent().get().usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("возвращать ожидаемый список комментариев к книге")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        List<Comment> actualComments = this.commentsRepository.findByBookId(EXISTING_BOOK_ID);
        Comment expectedComment1 = em.find(Comment.class, FIRST_EXISTING_COMMENT_ID);
        Comment expectedComment2 = em.find(Comment.class, SECOND_EXISTING_COMMENT_ID);
        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedComment1, expectedComment2);
    }

    @DisplayName("удалять заданный комментарий по его идентификатору")
    @Test
    void shouldCorrectDeleteCommentById() {
        Comment comment = em.find(Comment.class, FIRST_EXISTING_COMMENT_ID);
        assertThat(comment).isNotNull();

        this.commentsRepository.deleteById(FIRST_EXISTING_COMMENT_ID);

        comment = em.find(Comment.class, FIRST_EXISTING_COMMENT_ID);
        assertThat(comment).isNull();
    }
}
