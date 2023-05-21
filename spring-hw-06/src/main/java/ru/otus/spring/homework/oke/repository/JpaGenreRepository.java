package ru.otus.spring.homework.oke.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JpaGenreRepository implements GenreRepository {
    private final EntityManager entityManager;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            this.entityManager.persist(genre);
            return genre;
        } else {
            return this.entityManager.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.ofNullable(this.entityManager.find(Genre.class, id));
    }

    @Override
    public List<Genre> findByIds(Set<Long> ids) {
        String jpql = "select g from Genre g where g.id in (:ids)";
        TypedQuery<Genre> query = this.entityManager.createQuery(jpql, Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public List<Genre> findAll() {
        String jpql = "select g from Genre g";
        TypedQuery<Genre> query = this.entityManager.createQuery(jpql, Genre.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        Genre genreForRemove = this.entityManager.find(Genre.class, id);
        if (genreForRemove != null) {
            this.entityManager.remove(genreForRemove);
        }
    }
}
