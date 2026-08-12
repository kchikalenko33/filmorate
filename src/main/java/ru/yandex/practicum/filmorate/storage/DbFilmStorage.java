package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("DbFilm")
public class DbFilmStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> map = film.toMap();
        int id = jdbcInsert.executeAndReturnKey(map).intValue();

        film.setId(id);
        log.info("");
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rate = ? WHERE id = ?";
        return null;
    }

    @Override
    public List<Film> readAll() {
        String sql = "SELECT * FROM films";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs, rowNum));
        log.info("");
        return films;

    }

    @Override
    public Film readById(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapToFilm(rs, rowNum), id);
            log.info("");
            return film;
        } catch (Exception e) {
            log.warn("FilmStorage: фильм не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Фильм с id=" + id + " не найден");
        }
    }

    @Override
    public void deleteFilm(Integer id) {
        String sql = "DELETE FROM films WHERE id = ?";

        if(jdbcTemplate.update(sql, id) != 0) {
            log.info("");
        } else {
            log.info("");
        }
    }

    @Override
    public Object addLike(Integer id, Integer userId) {
        String sql = "INSERT INTO likes (ussr_id, film_id) VALUES (?, ?)";
        return null;
    }

    @Override
    public Object deleteLike(Integer id, Integer userId) {
        String sql = "DELETE FROM likes where user_id = ? AND film_id = ?";


        return null;
    }

    @Override
    public List<Film> readPopular(Integer count) {
        String sql = """
                SELECT f.*
                    FROM films f
                    LEFT JOIN likes l ON f.id = l.film_id
                    GROUP BY f.id
                    ORDER BY COUNT(l.user_id) DESC
                    LIMIT ?
                """;
        return List.of();
    }

    private Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .mpa(null) //todo нужно обратиться к базе и по id мпа получить объект целиком
                .build();
    }

}
