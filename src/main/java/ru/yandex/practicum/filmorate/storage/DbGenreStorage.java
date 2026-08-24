package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class DbGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre read(Integer id) {
        try {
            String sql = "SELECT * FROM genres WHERE id = ?";
            Genre genre = jdbcTemplate.queryForObject(sql, this::mapToGenre, id);

            log.info("DbGenreStorage: жанр найден, ID={}, название={}", id, genre.getName());
            return genre;
        } catch (EmptyResultDataAccessException e) {
            log.warn("DbGenreStorage: жанр не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Жанр с id=" + id + " не найден");
        }
    }

    @Override
    public Set<Genre> readAll() {
        String sql = "SELECT * FROM genres";
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql, this::mapToGenre));

        log.info("DbGenreStorage: получено {} жанров", genres.size());
        return genres;
    }

    public Set<Genre> readByIdFilm(Integer filmId) {
        String sql = """
                SELECT * FROM genres g
                JOIN film_genres fg
                ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(sql, this::mapToGenre, filmId));
        log.info("DbGenreStorage: получено {} жанров для фильма ID={}", genres.size(), filmId);
        return genres;
    }

    private Genre mapToGenre(ResultSet rs, Integer rowNum) {
        try {
            return Genre.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
