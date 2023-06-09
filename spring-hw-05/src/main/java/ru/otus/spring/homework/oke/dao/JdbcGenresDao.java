package ru.otus.spring.homework.oke.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.spring.homework.oke.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenresDao implements GenresDao {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Genre create(Genre genre) {
        String sql = "insert into genres (name) values (:name)";
        Map<String, Object> parameters = Collections.singletonMap("name", genre.getName());
        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sql, parameterSource, keyHolder);
        genre.setId(keyHolder.getKey().longValue());
        return genre;
    }

    @Override
    public Genre findById(long id) {
        String sql = "select id, name from genres where id = :id";
        Map<String, Object> parameters = Collections.singletonMap("id", id);
        return jdbc.queryForObject(sql, parameters, new GenreMapper());
    }

    @Override
    public List<Genre> findAll() {
        String sql = "select id, name from genres order by id";
        return jdbc.query(sql, new GenreMapper());
    }

    @Override
    public List<Genre> findAllUsed() {
        String sql = "select distinct g.id, g.name " +
                "from genres g inner join books_genres bg on (g.id = bg.genre_id) " +
                "order by g.id";
        return jdbc.query(sql, new GenreMapper());
    }

    @Override
    public List<Genre> findAllByBookId(long bookId) {
        String sql = "select distinct g.id, g.name " +
                "from genres g inner join books_genres bg on (g.id = bg.genre_id) " +
                "where bg.book_id = :book_id";
        return jdbc.query(sql, Collections.singletonMap("book_id", bookId), new GenreMapper());
    }

    @Override
    public List<Genre> findByIds(Set<Long> ids) {
        String sql = "select id, name from genres where id in (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);
        return jdbc.query(sql, parameters, new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        String sql = "delete from genres where id = :id";
        Map<String, Object> parameters = Collections.singletonMap("id", id);
        jdbc.update(sql, parameters);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);

        }
    }
}
