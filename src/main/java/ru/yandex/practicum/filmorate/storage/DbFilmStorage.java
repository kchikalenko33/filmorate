package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.util.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("DbFilm")
public class DbFilmStorage implements FilmStorage {
    private JdbcTemplate jdbcTemplate;
    private MpaStorage mpaStorage;
    private GenreStorage genreStorage;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film create(Film film) {
        Validator.validateFilm(film);
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> map = film.toMap();
        film.setMpa(mpaStorage.read(film.getMpa().getId()));
        int filmId = jdbcInsert.executeAndReturnKey(map).intValue();
        film.setId(filmId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String sql = """
                    INSERT INTO film_genres (film_id, genre_id)
                    VALUES (?, ?)
                    """;
            try {
                for (Genre genre : film.getGenres()) {
                    String checkSql = "SELECT COUNT(*) FROM genres WHERE id = ?";
                    Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, genre.getId());

                    if (count != null && count > 0) {
                        jdbcTemplate.update(sql, filmId, genre.getId());
                        log.info("DbFilmStorage: жанр ID={} добавлен к фильму ID={}", genre.getId(), filmId);
                    } else {
                        log.warn("DbFilmStorage: жанр с ID={} не найден в базе данных", genre.getId());
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Жанр с id=" + genre.getId() + " не найден");
                    }
                }
            } catch (Exception e) {
                log.error("DbFilmStorage: ошибка при добавлении жанров к фильму ID={}", filmId, e);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка при добавлении жанров");
            }

        }


        log.info("DbFilmStorage: фильм успешно создан, ID={}, название={}", filmId, film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        Validator.validateFilm(film);
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rate = ? WHERE id = ?";

        int count = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getId());

        if (count != 0) {
            log.info("DbFilmStorage: фильм с ID={} успешно обновлен", film.getId());
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Фильм с id =" + film.getId() + "не обновлен.");
        }

    }

    @Override
    public List<Film> readAll() {
        String sql = "SELECT * FROM films";

        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapToFilm(rs, rowNum));
        log.info("DbFilmStorage: получено {} фильмов", films.size());
        return films;

    }

    @Override
    public Film readById(Integer id) {
        String sql = "SELECT * FROM films WHERE id = ?";

        try {
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapToFilm(rs, rowNum), id);
            log.info("DbFilmStorage: фильм найден, ID={}, название={}", id, film.getName());
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

        if (jdbcTemplate.update(sql, id) != 0) {
            log.info("DbFilmStorage: фильм с ID={} успешно удален", id);
        } else {
            log.warn("DbFilmStorage: фильм с ID={} не найден для удаления", id);
        }
    }

    @Override
    public Object addLike(Integer id, Integer userId) {
        String sql = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, userId, id);

        log.info("DbFilmStorage: лайк добавлен, фильм ID={}, пользователь ID={}", id, userId);
        return Map.of("result", "ok");
    }

    @Override
    public Object deleteLike(Integer id, Integer userId) {
        String sql = "DELETE FROM likes where user_id = ? AND film_id = ?";

        if (jdbcTemplate.update(sql, userId, id) == 0) {
            log.warn("DbFilmStorage: лайк не удален, фильм ID={}, пользователь ID={}", id, userId);
        } else {
            log.info("DbFilmStorage: лайк удален, фильм ID={}, пользователь ID={}", id, userId);
        }
        return Map.of("result", "ok");
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
        List<Film> films = jdbcTemplate.query(sql, this::mapToFilm, count);

        log.info("DbFilmStorage: получено {} популярных фильмов", count);

        return films;
    }

    @Override
    public Set<Film> commonFilms(Integer userId, Integer friendId) {
        String sql = """
                SELECT *
                FROM films f
                LEFT JOIN likes l ON f.id = l.film_id
                LEFT JOIN likes l2 ON f.id = l2.film_id
                WHERE l.user_id = ? AND l2.user_id = ?
                GROUP BY f.id
                ORDER BY COUNT(l.user_id) DESC
                """;

        Set<Film> films = new HashSet<>(jdbcTemplate.query(sql, this::mapToFilm, userId, friendId));
        log.info("DbFilmStorage: получен список общих фильмов у пользователей с id = {} и c id = {}", userId, friendId);

        return films;
    }

    private Film mapToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .mpa(mpaStorage.read(rs.getInt("mpa_id")))
                .genres(genreStorage.readByIdFilm(rs.getInt("id")))
                .likes(readLikes(rs.getInt("id")))
                .build();
    }

    private Set<Integer> readLikes(Integer filmId) {
        String sql = "SELECT * FROM likes WHERE film_id = ?";

        Set<Integer> likes = new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("film_id"), filmId));
        log.info("DbFilmStorage: получен список лайков по filmId ={}", filmId);
        return likes;
    }
}
