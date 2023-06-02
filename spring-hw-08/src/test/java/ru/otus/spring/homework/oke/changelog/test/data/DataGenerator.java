package ru.otus.spring.homework.oke.changelog.test.data;

import org.springframework.stereotype.Component;
import ru.otus.spring.homework.oke.model.Author;
import ru.otus.spring.homework.oke.model.Book;
import ru.otus.spring.homework.oke.model.Comment;
import ru.otus.spring.homework.oke.model.Genre;

import java.util.List;

@Component
public class DataGenerator {
    public static Author getFirstAuthor() {
        return new Author("1", "Имя1", "Среднее имя1", null, "Фамилия1");
    }

    public static Author getSecondAuthor() {
        return new Author("2", "Имя2", null, "Отчество2", "Фамилия2");
    }

    public static Author getThirdAuthor() {
        return new  Author("3", "Имя3", null, "Отчество3", "Фамилия3");
    }

    public static Book getFirstBook() {
        Genre genre1 = new Genre("Жанр1");
        Genre genre2 = new Genre("Жанр2");
        List<Genre> genres = List.of(genre1, genre2);
        Author author = getFirstAuthor();
        return new Book("1", "Книга1", "Описание1", author, genres);
    }

    public static Book getSecondBook() {
        Genre genre1 = new Genre("Жанр1");
        Genre genre2 = new Genre("Жанр2");
        List<Genre> genres = List.of(genre1, genre2);
        Author author = getFirstAuthor();
        return new Book("2", "Книга2", "Описание2", author, genres);
    }

    public static Book getThirdBook() {
        Genre genre = new Genre("Жанр3");
        Author author = getSecondAuthor();
        return new Book("3", "Книга3", "Описание3", author, List.of(genre));
    }

    public static Comment getFirstComment() {
        Book book = getFirstBook();
        return new Comment("1", "Коммент1", book);
    }

    public static Comment getSecondComment() {
        Book book = getFirstBook();
        return new Comment("2", "Коммент2", book);
    }

    public static Comment getThirdComment() {
        Book book = getSecondBook();
        return new Comment("3", "Коммент3", book);
    }
}
