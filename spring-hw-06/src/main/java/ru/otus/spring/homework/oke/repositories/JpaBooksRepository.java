package ru.otus.spring.homework.oke.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBooksRepository implements BooksRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            this.entityManager.persist(book);
            return book;
        } else {
            return this.entityManager.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(this.entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) {
        String jpql = "select b from Book b where b.author.id = :authorId";
        TypedQuery<Book> query = this.entityManager.createQuery(jpql, Book.class);
        query.setParameter("authorId", authorId);
        return query.getResultList();
    }

    @Override
    public List<Book> findAll() {
        String jpql = "select distinct b from Book b join fetch b.author";
        TypedQuery<Book> query = this.entityManager.createQuery(jpql, Book.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Book bookForRemove = this.entityManager.find(Book.class, id);
        if (bookForRemove != null) {
            this.entityManager.remove(bookForRemove);
        }
    }
}
