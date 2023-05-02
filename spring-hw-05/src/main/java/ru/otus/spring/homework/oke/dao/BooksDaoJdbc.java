package ru.otus.spring.homework.oke.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dao.ext.BookGenreRelation;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.domain.Book;
import ru.otus.spring.homework.oke.domain.Genre;
import ru.otus.spring.homework.oke.exceptions.AuthorNotFoundException;
import ru.otus.spring.homework.oke.exceptions.BookNotFoundException;
import ru.otus.spring.homework.oke.exceptions.GenreNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BooksDaoJdbc implements BooksDao {
    private final NamedParameterJdbcOperations jdbc;

    private final GenresDao genresDao;

    @Transactional
    @Override
    public Book create(Book book) {
        String sql = "insert into books (title, description, author_id)  values " +
                "(:title, :description, :author_id)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        parameters.put("description", book.getDescription());
        parameters.put("author_id", book.getAuthor().getId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(sql, parameterSource, keyHolder);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Автор с указанным идентификатором " + book.getAuthor().getId() +
                    " не найден. Возможно создание книги только c существующим автором";
            throw new AuthorNotFoundException(errorMessage, e);
        }
        long insertedId = keyHolder.getKey().longValue();
        createGenresRelations(insertedId, book.getGenres());
        return this.findById(insertedId);
    }

    @Transactional
    @Override
    public Book update(Book book) {
        String sql = "update books set title = :title, description = :description, " +
                "author_id = :author_id where id = :id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        parameters.put("description", book.getDescription());
        parameters.put("author_id", book.getAuthor().getId());
        parameters.put("id", book.getId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        try {
            int affectedRows = jdbc.update(sql, parameterSource);
            if (affectedRows == 0) {
                String errorMessage = "Книга с указанным идентификатором " + book.getId() + " не найдена. Укажите " +
                        "существующий идентификатор для обновления";
                throw new BookNotFoundException(errorMessage);
            }
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Автор с указанным идентификатором " + book.getAuthor().getId() +
                    " не найден. Укажите существующего автора";
            throw new AuthorNotFoundException(errorMessage, e);
        }
        updateGenresRelations(book.getId(), book.getGenres());
        return this.findById(book.getId());
    }

    /**
     * Метод выполняет поиск книги по её идентификатору
     * Поиск выполняется в 2 запроса:
     * 1. Запрос книги и её автора (join)
     * 2. Запрос всех жанров по идентификатору книги через GenresDao (там выполняется join жанров и связок книги с
     * жанрами)
     *
     * @param id идентификатор книги
     * @return найденная книга
     */
    @Override
    public Book findById(long id) {
        String sql = "select b.id, " +
                "b.title, " +
                "b.description, " +
                "b.author_id, " +
                "a.name, " +
                "a.middle_name, " +
                "a.patronymic, " +
                "a.surname " +
                "from books b inner join authors a on (b.author_id = a.id) " +
                "where b.id = :id";
        Book foundBook = jdbc.queryForObject(sql, Collections.singletonMap("id", id), new BookMapper());
        List<Genre> bookGenres = genresDao.findAllByBookId(foundBook.getId());
        foundBook.getGenres().addAll(bookGenres);
        return foundBook;
    }

    @Override
    public List<Book> findAll() {
        List<BookGenreRelation> relations = this.getAllGenresRelations();
        List<Genre> genres = genresDao.findAllUsed();
        String sql = "select b.id, " +
                "b.title, " +
                "b.description, " +
                "b.author_id, " +
                "a.name, " +
                "a.middle_name, " +
                "a.patronymic, " +
                "a.surname " +
                "from books b inner join authors a on (b.author_id = a.id)";
        List<Book> books = jdbc.query(sql, new BookMapper());
        this.mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        deleteGenresRelations(id);
        String sql = "delete from books where id = :id";
        Map<String, Object> parameters = Collections.singletonMap("id", id);
        jdbc.update(sql, parameters);
    }

    /**
     * Метод создает связки книги и её жанров.
     * До момента вызова метода жанры уже должны существовать в таблице жанров
     *
     * @param bookId          идентификатор книги, для которой необходимо создать привязку к жанрам
     * @param genreRefsForAdd жанры, для которых необходимо создать привязку
     */
    private void createGenresRelations(long bookId, Set<Genre> genreRefsForAdd) {
        String sql = "insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)";
        if (genreRefsForAdd != null && !genreRefsForAdd.isEmpty()) {
            List<Map<String, Object>> batchParameters = new ArrayList<>(genreRefsForAdd.size());
            for (Genre genre : genreRefsForAdd) {
                Map<String, Object> parameters = Map.of("book_id", bookId, "genre_id", genre.getId());
                batchParameters.add(parameters);
            }
            try {
                jdbc.batchUpdate(sql, batchParameters.toArray(new Map[0]));
            } catch (DataIntegrityViolationException e) {
                String errorMessage = "Один или несколько из указанных жанров не найден. Книга может иметь только " +
                        "существующие жанры, либо не иметь никаких";
                throw new GenreNotFoundException(errorMessage, e);
            }
        }
    }

    /**
     * Метод выполняет обновление связки книги и её жанров.
     * Добавляет в таблицу связей новые записи для всех новых связок (которых не было до вызова обновления)
     * Удаляет из таблицы связей записи для всех связок с жанрами, которых нет в новом наборе.
     *
     * @param bookId    идентификатор книги
     * @param newGenres новый набор жанров для книги
     */
    private void updateGenresRelations(long bookId, Set<Genre> newGenres) {
        if (newGenres == null || newGenres.isEmpty()) {
            deleteGenresRelations(bookId);
        } else {
            List<Genre> existentGenres = this.genresDao.findAllByBookId(bookId);
            if (existentGenres == null || existentGenres.isEmpty()) {
                createGenresRelations(bookId, newGenres);
            } else {
                Set<Genre> genresRefForAdd = this.getGenresForAdd(newGenres, existentGenres);
                Set<Genre> genresRefForRemove = this.getGenresForRemove(newGenres, existentGenres);
                this.createGenresRelations(bookId, genresRefForAdd);
                this.deleteGenresRelations(bookId, genresRefForRemove);
            }
        }
    }

    /**
     * Метод осуществляет поиск всех жанров из нового набора, которых нет в старом наборе
     *
     * @param newGenres      новый набор жанров
     * @param existentGenres старый набор жанров
     * @return набор жанров, к которым необходимо привязать книгу
     */
    private Set<Genre> getGenresForAdd(Set<Genre> newGenres, List<Genre> existentGenres) {
        Map<Long, Genre> existentGenresMap = existentGenres.stream().collect(Collectors.toMap(Genre::getId,
                Function.identity()));
        Set<Genre> genresForAdd = new HashSet<>();
        newGenres.forEach(genre -> {
            if (!existentGenresMap.containsKey(genre.getId())) {
                genresForAdd.add(genre);
            }
        });
        return genresForAdd;
    }

    /**
     * Метод осуществляет поиск всех жанров из старого набора, которых нет в новом наборе жанров
     *
     * @param newGenres      новый набор жанров
     * @param existentGenres старый набор жанров
     * @return набор жанров, от которых необходимо отвязать книгу
     */
    private Set<Genre> getGenresForRemove(Set<Genre> newGenres, List<Genre> existentGenres) {
        Map<Long, Genre> newGenresMap = newGenres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        Set<Genre> genresForRemove = new HashSet<>();
        existentGenres.forEach(genre -> {
            if (!newGenresMap.containsKey(genre.getId())) {
                genresForRemove.add(genre);
            }
        });
        return genresForRemove;
    }

    /**
     * Метод выполняет удаление всех привязок жанров к книге
     *
     * @param bookId идентификатор книги
     */
    private void deleteGenresRelations(long bookId) {
        String sql = "delete from books_genres where book_id = :book_id";
        Map<String, Object> parameters = Collections.singletonMap("book_id", bookId);
        jdbc.update(sql, parameters);
    }

    /**
     * Метод выполняет удаление привязок конкретных жанров к книге
     *
     * @param bookId             идентификатор книги
     * @param genreRefsForRemove перечень жанров для удаления привязки к книге
     */
    private void deleteGenresRelations(long bookId, Set<Genre> genreRefsForRemove) {
        String sql = "delete from books_genres where book_id = :book_id and genre_id = :genre_id";
        if (genreRefsForRemove != null && !genreRefsForRemove.isEmpty()) {
            List<Map<String, Object>> batchParameters = new ArrayList<>(genreRefsForRemove.size());
            for (Genre genre : genreRefsForRemove) {
                Map<String, Object> parameters = Map.of("book_id", bookId, "genre_id", genre.getId());
                batchParameters.add(parameters);
            }
            jdbc.batchUpdate(sql, batchParameters.toArray(new Map[0]));
        }
    }

    private List<BookGenreRelation> getAllGenresRelations() {
        String sql = "select book_id, genre_id from books_genres";
        return jdbc.query(sql, new BookGenreRelationMapper());
    }

    private void mergeBooksInfo(List<Book> books, List<Genre> genres, List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        Map<Long, Genre> genresMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        relations.forEach(r -> {
            if (booksMap.containsKey(r.getBookId()) && genresMap.containsKey(r.getGenreId())) {
                booksMap.get(r.getBookId()).getGenres().add(genresMap.get(r.getGenreId()));
            }
        });
    }

    private static class BookMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author bookAuthor = new Author(rs.getLong("author_id"), rs.getString("name"),
                    rs.getString("middle_name"), rs.getString("patronymic"),
                    rs.getString("surname"));
            Set<Genre> bookGenres = new HashSet<>();
            Book book = new Book(rs.getLong("id"), rs.getString("title"),
                    rs.getString("description"), bookAuthor, bookGenres);
            return book;
        }
    }

    private static class BookGenreRelationMapper implements RowMapper<BookGenreRelation> {

        @Override
        public BookGenreRelation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"));
        }
    }
}
