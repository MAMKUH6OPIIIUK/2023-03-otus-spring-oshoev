package ru.otus.spring.homework.oke.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    private final EntityManager entityManager;

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            this.entityManager.persist(comment);
            return comment;
        } else {
            return this.entityManager.merge(comment);
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(this.entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        String jpql = "select c from Comment c where c.book.id = :bookId";
        TypedQuery<Comment> query = this.entityManager.createQuery(jpql, Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Comment commentForRemove = this.entityManager.find(Comment.class, id);
        if (commentForRemove != null) {
            this.entityManager.remove(commentForRemove);
        }
    }
}
