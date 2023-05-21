package ru.otus.spring.homework.oke.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorId(Long authorId);

    @Override
    @Query("select distinct b from Book b join fetch b.author")
    List<Book> findAll();

}
