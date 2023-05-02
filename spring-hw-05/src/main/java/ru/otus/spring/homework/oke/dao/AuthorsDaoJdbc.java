package ru.otus.spring.homework.oke.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.domain.Author;
import ru.otus.spring.homework.oke.exceptions.AuthorBooksFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class AuthorsDaoJdbc implements AuthorsDao {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Author create(Author author) {
        String sql = "insert into authors (name, middle_name, patronymic, surname) values " +
                "(:name, :middle_name, :patronymic, :surname)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", author.getName());
        parameters.put("middle_name", author.getMiddleName());
        parameters.put("patronymic", author.getPatronymic());
        parameters.put("surname", author.getSurname());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameterSource, keyHolder);
        return new Author(keyHolder.getKey().longValue(), author.getName(), author.getMiddleName(),
                author.getPatronymic(), author.getSurname());
    }

    @Override
    public Author findById(long id) {
        String sql = "select id, name, middle_name, patronymic, surname from authors where id = :id";
        Map<String, Object> parameters = Collections.singletonMap("id", id);
        return jdbc.queryForObject(sql, parameters, new AuthorMapper());
    }

    @Override
    public List<Author> findAll() {
        String sql = "select id, name, middle_name, patronymic, surname from authors order by id";
        return jdbc.query(sql, new AuthorMapper());
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from authors where id = :id";
        Map<String, Object> parameters = Collections.singletonMap("id", id);
        try {
            jdbc.update(sql, parameters);
        } catch (DataIntegrityViolationException e) {
            String errorMessage = "Найдены книги данного автора. Сначала удалите их";
            throw new AuthorBooksFoundException(errorMessage, e);
        }
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String middleName = resultSet.getString("middle_name");
            String patronymic = resultSet.getString("patronymic");
            String surname = resultSet.getString("surname");
            return new Author(id, name, middleName, patronymic, surname);
        }
    }
}
