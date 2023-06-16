package ru.otus.spring.homework.oke.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.dto.validation.CreateCommentScope;
import ru.otus.spring.homework.oke.dto.validation.UpdateCommentScope;
import ru.otus.spring.homework.oke.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/comment")
@Validated
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentResponseDto> findByBookId(@RequestParam(name = "bookId") Long bookId) {
        return this.commentService.findByBookId(bookId);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public CommentResponseDto create(@Validated(CreateCommentScope.class) @RequestBody CommentRequestDto comment) {
        return this.commentService.create(comment);
    }

    @PutMapping(value = "/{id}")
    public void update(@PathVariable("id") Long id,
                       @Validated(UpdateCommentScope.class) @RequestBody CommentRequestDto comment) {
        comment.setId(id);
        this.commentService.update(comment);
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public void deleteById(@PathVariable("id") Long id) {
        this.commentService.deleteById(id);
    }
}
