package ru.otus.spring.homework.oke.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.homework.oke.dao.ext.BookGenreRelation;
import ru.otus.spring.homework.oke.domain.Book;

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
public class JdbcBooksDao implements BooksDao {
    private final NamedParameterJdbcOperations jdbc;

    /**
     * Метод выполняет создание книги в БД. К моменту вызова метода автор и жанры книги должны существовать в базе
     * данных
     *
     * @param book книга для создания
     * @return созданная книга с установленным идентификатором
     */
    @Transactional
    @Override
    public Book create(Book book) {
        String sql = "insert into books (title, description, author_id)  values " +
                "(:title, :description, :author_id)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        parameters.put("description", book.getDescription());
        parameters.put("author_id", book.getAuthorId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameterSource, keyHolder);
        long insertedId = keyHolder.getKey().longValue();
        createGenresRelations(insertedId, book.getGenreIds());
        book.setId(insertedId);
        return book;
    }

    /**
     * Метод выполняет обновление книги в БД. К моменту вызова метода сама книга должна существовать в базе, аналогично
     * новые автор и жанры книги должны существовать в базе данных
     *
     * @param book данные книги для обновления
     * @return обновленная книга
     */
    @Transactional
    @Override
    public Book update(Book book) {
        String sql = "update books set title = :title, description = :description, " +
                "author_id = :author_id where id = :id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", book.getTitle());
        parameters.put("description", book.getDescription());
        parameters.put("author_id", book.getAuthorId());
        parameters.put("id", book.getId());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        jdbc.update(sql, parameterSource);
        updateGenresRelations(book.getId(), book.getGenreIds());
        return book;
    }

    /**
     * Метод выполняет поиск книги по её идентификатору
     * Поиск выполняется в 2 запроса:
     * 1. Запрос книги
     * 2. Запрос всех идентификаторов жанров книги
     *
     * @param id идентификатор книги
     * @return найденная книга
     */
    @Override
    public Book findById(long id) {
        String sql = "select b.id, b.title, b.description, b.author_id from books b where b.id = :id";
        Book foundBook = jdbc.queryForObject(sql, Collections.singletonMap("id", id), new BookMapper());
        List<Long> bookGenreIds = this.getGenresRelationByBookId(id);
        foundBook.getGenreIds().addAll(bookGenreIds);
        return foundBook;
    }

    /**
     * Метод выполняет поиск всех книг
     * Поиск выполняется в 2 запроса:
     * 1. Запрос всех книг
     * 2. Запрос всех связей книг с жанрами
     *
     * @return список найденных книг
     */
    @Override
    public List<Book> findAll() {
        List<BookGenreRelation> relations = this.getAllGenresRelations();
        String sql = "select b.id, b.title, b.description, b.author_id from books b";
        List<Book> books = jdbc.query(sql, new BookMapper());
        this.mergeBooksInfo(books, relations);
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
     * @param genreRefsForAdd идентификаторы жанров, для которых необходимо создать привязку
     */
    private void createGenresRelations(long bookId, Set<Long> genreRefsForAdd) {
        String sql = "insert into books_genres (book_id, genre_id) values (:book_id, :genre_id)";
        if (genreRefsForAdd != null && !genreRefsForAdd.isEmpty()) {
            List<Map<String, Object>> batchParameters = new ArrayList<>(genreRefsForAdd.size());
            for (Long genreId : genreRefsForAdd) {
                Map<String, Object> parameters = Map.of("book_id", bookId, "genre_id", genreId);
                batchParameters.add(parameters);
            }
            jdbc.batchUpdate(sql, batchParameters.toArray(new Map[0]));
        }
    }

    /**
     * Метод выполняет обновление связки книги и её жанров.
     * Добавляет в таблицу связей новые записи для всех новых связок (которых не было до вызова обновления)
     * Удаляет из таблицы связей записи для всех связок с жанрами, которых нет в новом наборе.
     *
     * @param bookId      идентификатор книги
     * @param newGenreIds новый набор жанров для книги
     */
    private void updateGenresRelations(long bookId, Set<Long> newGenreIds) {
        if (newGenreIds == null || newGenreIds.isEmpty()) {
            deleteGenresRelations(bookId);
        } else {
            Book existentBook = this.findById(bookId);
            Set<Long> existentGenres = existentBook.getGenreIds();
            if (existentGenres == null || existentGenres.isEmpty()) {
                createGenresRelations(bookId, newGenreIds);
            } else {
                Set<Long> genresRefForAdd = newGenreIds
                        .stream()
                        .filter(element -> !existentGenres.contains(element)).collect(Collectors.toSet());
                Set<Long> genresRefForRemove = existentGenres
                        .stream()
                        .filter(element -> !newGenreIds.contains(element)).collect(Collectors.toSet());
                this.createGenresRelations(bookId, genresRefForAdd);
                this.deleteGenresRelations(bookId, genresRefForRemove);
            }
        }
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
     * @param genreRefsForRemove перечень идентификаторов жанров для удаления привязки к книге
     */
    private void deleteGenresRelations(long bookId, Set<Long> genreRefsForRemove) {
        String sql = "delete from books_genres where book_id = :book_id and genre_id = :genre_id";
        if (genreRefsForRemove != null && !genreRefsForRemove.isEmpty()) {
            List<Map<String, Object>> batchParameters = new ArrayList<>(genreRefsForRemove.size());
            for (Long genreId : genreRefsForRemove) {
                Map<String, Object> parameters = Map.of("book_id", bookId, "genre_id", genreId);
                batchParameters.add(parameters);
            }
            jdbc.batchUpdate(sql, batchParameters.toArray(new Map[0]));
        }
    }

    private List<BookGenreRelation> getAllGenresRelations() {
        String sql = "select book_id, genre_id from books_genres";
        return jdbc.query(sql, new BookGenreRelationMapper());
    }

    private List<Long> getGenresRelationByBookId(Long bookId) {
        String sql = "select genre_id from books_genres where book_id = :book_id";
        Map<String, Object> parameters = Collections.singletonMap("book_id", bookId);
        return jdbc.query(sql, parameters, (rs, rowNum) -> rs.getLong("genre_id"));
    }

    private void mergeBooksInfo(List<Book> books, List<BookGenreRelation> relations) {
        Map<Long, Book> booksMap = books.stream().collect(Collectors.toMap(Book::getId, Function.identity()));
        relations.forEach(r -> {
            if (booksMap.containsKey(r.getBookId())) {
                booksMap.get(r.getBookId()).getGenreIds().add(r.getGenreId());
            }
        });
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Set<Long> bookGenreIds = new HashSet<>();
            Book book = new Book(rs.getLong("id"), rs.getString("title"),
                    rs.getString("description"), rs.getLong("author_id"), bookGenreIds);
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
