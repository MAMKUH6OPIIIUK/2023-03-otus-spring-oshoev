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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.dto.validation.CreateCommentScope;
import ru.otus.spring.homework.oke.dto.validation.UpdateCommentScope;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @GetMapping
    public Flux<CommentResponseDto> findByBookId(@RequestParam(name = "bookId") String bookId) {
        return this.commentRepository.findByBookId(bookId)
                .map(commentMapper::mapToCommentResponseDto);
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public Mono<CommentResponseDto> create(@Validated(CreateCommentScope.class) @RequestBody
                                                   CommentRequestDto comment) {
        return this.bookRepository.findById(comment.getBookId())
                .switchIfEmpty(Mono.fromCallable(() -> {
                    throw new NotFoundException("Книга с указанным идентификатором " + comment.getBookId() +
                            " не найдена");
                }))
                .map(book -> this.commentMapper.mapToComment(comment, book))
                .flatMap(this.commentRepository::save)
                .map(commentMapper::mapToCommentResponseDto);
    }

    @PutMapping(value = "/{id}")
    public Mono<Void> update(@PathVariable("id") String id,
                             @Validated(UpdateCommentScope.class) @RequestBody CommentRequestDto comment) {
        comment.setId(id);
        return this.commentRepository.findById(id)
                .switchIfEmpty(Mono.fromCallable(() -> {
                    throw new NotFoundException("Комментарий с указанным идентификатором " + id +
                            " не найден");
                }))
                .map(commentForUpdate -> this.commentMapper.mergeCommentInfo(commentForUpdate, comment))
                .flatMap(this.commentRepository::save)
                .then();
    }

    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return this.commentRepository.deleteById(id);
    }
}
