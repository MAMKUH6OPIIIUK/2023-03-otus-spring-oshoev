package ru.otus.spring.homework.oke.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.models.Author;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaAuthorsRepository implements AuthorsRepository {
    private final EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            this.entityManager.persist(author);
            return author;
        } else {
            return this.entityManager.merge(author);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(this.entityManager.find(Author.class, id));
    }

    @Override
    public List<Author> findAll() {
        String jpql = "select a from Author a";
        TypedQuery<Author> query = this.entityManager.createQuery(jpql, Author.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Author authorForRemove = this.entityManager.find(Author.class, id);
        if (authorForRemove != null) {
            this.entityManager.remove(authorForRemove);
        }
    }
}
