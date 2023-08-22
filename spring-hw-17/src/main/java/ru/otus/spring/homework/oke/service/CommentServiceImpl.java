package ru.otus.spring.homework.oke.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dto.CommentRequestDto;
import ru.otus.spring.homework.oke.dto.CommentResponseDto;
import ru.otus.spring.homework.oke.exception.NotFoundException;
import ru.otus.spring.homework.oke.mapper.CommentMapper;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;
import ru.otus.spring.homework.oke.repository.BookRepository;
import ru.otus.spring.homework.oke.repository.CommentRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    private final MutableAclService mutableAclService;

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public CommentResponseDto create(CommentRequestDto commentRequestDto) {
        Book commentBook = this.validateBook(commentRequestDto.getBookId());
        Comment commentForCreate = this.commentMapper.mapToComment(commentRequestDto, commentBook);
        Comment createdComment = this.commentRepository.save(commentForCreate);
        grantPermissions(createdComment.getId());
        return this.commentMapper.mapToCommentResponseDto(createdComment);
    }

    @PreAuthorize("hasPermission(#commentRequestDto, 'WRITE') || hasRole('ADMIN')")
    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public void update(CommentRequestDto commentRequestDto) {
        Comment commentForUpdate = this.validateComment(commentRequestDto.getId());
        commentForUpdate = this.commentMapper.mergeCommentInfo(commentForUpdate, commentRequestDto);
        this.commentRepository.save(commentForUpdate);
    }

    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional(readOnly = true)
    public CommentResponseDto findById(Long id) {
        Comment foundComment = this.validateComment(id);
        CommentResponseDto result = this.commentMapper.mapToCommentResponseDto(foundComment);
        return result;
    }

    @Override
    @CircuitBreaker(name = "long-running-business-method")
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByBookId(Long bookId) {
        List<Comment> foundComments = this.commentRepository.findByBookId(bookId);
        if (foundComments.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<CommentResponseDto> result = foundComments
                .stream()
                .map(this.commentMapper::mapToCommentResponseDto)
                .collect(Collectors.toList());
        return result;
    }

    @PreAuthorize("hasPermission(#id, 'ru.otus.spring.homework.oke.dto.CommentRequestDto', 'WRITE') " +
            "|| hasRole('ADMIN')")
    @Override
    @CircuitBreaker(name = "default-business-method")
    @Transactional
    public void deleteById(Long id) {
        this.commentRepository.deleteById(id);
    }

    @Override
    @CircuitBreaker(name = "default-business-method", fallbackMethod = "fallbackCount")
    public long countByCreatedOnAfter(Date thresholdDate) {
        return this.commentRepository.countByCreatedOnAfter(thresholdDate);
    }

    private long fallbackCount(Date thresholdDate, Throwable e) {
        log.warn("Вызван fallback метод для подсчета количества новых комментариев, причина: {}", e.getMessage());
        return 0;
    }

    private Comment validateComment(Long id) {
        Comment comment = this.commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с указанным идентификатором " + id +
                        " не найден"));
        return comment;
    }

    private Book validateBook(Long bookId) {
        Book book = this.bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("Книга с указанным идентификатором " + bookId +
                        " не найдена"));
        return book;
    }

    /**
     * Метод выдает разрешения на редактирование и удаление комментария его автору
     * @param id идентификатор комментария
     */
    private void grantPermissions(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(CommentRequestDto.class, id);
        MutableAcl acl = mutableAclService.createAcl(oid);
        acl.setOwner(owner);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, owner, true);
        mutableAclService.updateAcl(acl);
    }
}
