package ru.otus.spring.homework.oke.shell;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.formatters.CommentFormatter;
import ru.otus.spring.homework.oke.services.CommentsService;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class CommentsCLI {
    private final CommentsService commentsService;

    private final CommentFormatter commentFormatter;

    @ShellMethod(value = "Добавить новый комментарий к книге", key = {"cc", "create comment"})
    public String createComment(@ShellOption(help = "Текст комментария", value = {"t", "text"})
                                @Size(min = 1, max = 1000)
                                        String text,
                                @ShellOption(help = "Идентификатор книги", value = {"bi", "bookId"})
                                @Positive Long bookId) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(text, bookId);
        CommentResponseDto createdComment = this.commentsService.create(commentRequestDto);
        return "Успешно добавлен комментарий! " + this.commentFormatter.formatComment(createdComment, 0);
    }

    @ShellMethod(value = "Обновить текст комментария", key = {"uc", "update comment"})
    public String updateComment(@ShellOption(help = "Идентификатор комментария", value = {"i", "id"})
                                @Positive
                                        Long id,
                                @ShellOption(help = "Текст комментария", value = {"t", "text"})
                                @Size(min = 1, max = 1000)
                                        String text,
                                @ShellOption(help = "Идентификатор книги", value = {"bi", "bookId"})
                                @Positive
                                        Long bookId) {
        CommentRequestDto commentRequestDto = new CommentRequestDto(text, bookId);
        this.commentsService.update(id, commentRequestDto);
        return "Успешно обновлен комментарий!";
    }

    @ShellMethod(value = "Получить комментарий по его идентификатору", key = {"gc", "get comment"})
    public String getCommentById(@ShellOption(help = "Идентификатор комментария", value = {"i", "id"})
                                 @Positive
                                         Long id) {
        CommentResponseDto comment = this.commentsService.findById(id);
        return this.commentFormatter.formatComment(comment, 0);
    }

    @ShellMethod(value = "Получить все комментарии по идентификатору книги", key = {"gbc", "get book comments"})
    public String getCommentsByBookId(@ShellOption(help = "Идентификатор книги", value = {"bi", "bookId"})
                                      @Positive
                                              Long bookId) {
        List<CommentResponseDto> comments = this.commentsService.findByBookId(bookId);
        return this.commentFormatter.formatComments(comments, 0);
    }

    @ShellMethod(value = "Удалить комментарий", key = {"rc", "remove comment"})
    public void removeComment(@ShellOption(help = "Идентификатор комментария", value = {"i", "id"})
                              @Positive Long id) {
        this.commentsService.deleteById(id);
    }

}
