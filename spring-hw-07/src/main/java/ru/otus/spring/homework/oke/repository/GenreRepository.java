package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;
import java.util.Set;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(Set<Long> ids);

    @Override
    List<Genre> findAll();

}
