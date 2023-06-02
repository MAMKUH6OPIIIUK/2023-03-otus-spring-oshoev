package ru.otus.spring.homework.oke.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.spring.homework.oke.changelog.test.data.DataGenerator;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Репозиторий для работы с комментариями должен ")
@DataMongoTest
public class CommentRepositoryTest {
    private static final String EXISTING_BOOK_ID = "1";

    private static final String NON_EXISTING_COMMENT_ID = "non_existing_comment";

    private static final String FIRST_EXISTING_COMMENT_ID = "1";

    private static final String THIRD_EXISTING_COMMENT_ID = "3";

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("добавлять комментарий к книге в БД")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldInsertComment() {
        Book existingBook = DataGenerator.getThirdBook();
        Comment comment = new Comment(NON_EXISTING_COMMENT_ID, "Новый комментарий", existingBook);

        this.commentRepository.save(comment);

        Optional<Comment> actualComment = this.commentRepository.findById(NON_EXISTING_COMMENT_ID);
        assertThat(actualComment).isPresent().get().usingRecursiveComparison().isEqualTo(comment);
    }

    @DisplayName("обновлять комментарий к книге в БД")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void  shouldUpdateComment() {
        Comment comment = this.commentRepository.findById(THIRD_EXISTING_COMMENT_ID).get();
        Book book = DataGenerator.getThirdBook();
        Comment commentForUpdate = comment;
        commentForUpdate.setBook(book);
        commentForUpdate.setText("Новый текст");
        this.commentRepository.save(commentForUpdate);

        comment = this.commentRepository.findById(THIRD_EXISTING_COMMENT_ID).get();
        assertThat(comment).usingRecursiveComparison().isEqualTo(commentForUpdate);
        assertEquals(book, book);
    }

    @DisplayName("возвращать ожидаемый комментарий по его id")
    @Test
    void shouldReturnExpectedCommentById() {
        Comment actualComment = this.commentRepository.findById(FIRST_EXISTING_COMMENT_ID).get();
        Comment expectedComment = DataGenerator.getFirstComment();
        assertThat(actualComment).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName("возвращать ожидаемый список комментариев к книге")
    @Test
    void shouldReturnExpectedCommentsByBookId() {
        List<Comment> actualComments = this.commentRepository.findByBookId(EXISTING_BOOK_ID);
        Comment expectedComment1 = DataGenerator.getFirstComment();
        Comment expectedComment2 = DataGenerator.getSecondComment();
        assertThat(actualComments)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(expectedComment1, expectedComment2);
    }

    @DisplayName("удалять заданный комментарий по его идентификатору")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldCorrectDeleteCommentById() {
        Optional<Comment> comment = this.commentRepository.findById(FIRST_EXISTING_COMMENT_ID);
        assertThat(comment).isPresent();

        this.commentRepository.deleteById(FIRST_EXISTING_COMMENT_ID);

        comment = this.commentRepository.findById(FIRST_EXISTING_COMMENT_ID);
        assertThat(comment).isNotPresent();
    }
}
