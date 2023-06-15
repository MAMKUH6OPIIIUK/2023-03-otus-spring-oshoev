package ru.otus.spring.homework.oke.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping(value = "/api/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CommentResponseDto> findByBookId(@RequestParam(name = "bookId") Long bookId) {
        return this.commentService.findByBookId(bookId);
    }

    @PostMapping(value = "/api/comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponseDto> create(@Valid @RequestBody CommentRequestDto comment) {
        CommentResponseDto createdComment = this.commentService.create(comment);
        return new ResponseEntity<>(createdComment, new HttpHeaders(),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/api/comment/{id}")
    public HttpStatus update(@PathVariable("id") Long id, @Valid @RequestBody CommentRequestDto comment) {
        comment.setId(id);
        this.commentService.update(comment);
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/api/comment/{id}")
    public HttpStatus deleteById(@PathVariable("id") Long id) {
        this.commentService.deleteById(id);
        return HttpStatus.OK;
    }
}
