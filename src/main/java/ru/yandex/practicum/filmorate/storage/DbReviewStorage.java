package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class DbReviewStorage implements ReviewStorage{
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public DbReviewStorage(JdbcTemplate jdbcTemplate, FilmStorage filmStorage, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Review create(Review review) {
        if (review.getUserId() == null || review.getFilmId() == null) {
            log.warn("юзер или фильм нул");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        filmStorage.readById(review.getFilmId());
        userStorage.readById(review.getUserId());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("reviewId");

        Integer reviewId = insert.executeAndReturnKey(review.toMap()).intValue();
        review.setReviewId(reviewId);
        log.info("DbReviewStorage: отзыв c ID={} создан",reviewId);
        return review;
    }

    @Override
    public Review update(Review review) {
        if (review.getUserId() == null || review.getFilmId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        filmStorage.readById(review.getFilmId());
        userStorage.readById(review.getUserId());
        readById(review.getReviewId());


        String sql = """
                UPDATE reviews
                SET content = ?, isPositive = ?
                WHERE reviewId = ?
                """;
        int count = jdbcTemplate.update(sql, review.getContent(), review.getIsPositive(), review.getReviewId());

        if (count != 0) {
            log.info("DbReviewStorage: отзыв с ID={} успешно обновлен", review.getReviewId());
            return review;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Отзыв с ID=" + review.getReviewId() + "не обновлен");
        }
    }

    @Override
    public Review readById(Integer reviewId) {
        String sql = """
                SELECT *
                FROM reviews
                WHERE reviewId = ?
                """;
        try {
            Review review = jdbcTemplate.queryForObject(sql, this::mapToReview, reviewId);
            log.info("DbReviewStorage: отзыв найден, ID={}", reviewId);
            return review;
        } catch (Exception e) {
            log.warn("DbReviewStorage: отзыв не найден, ID={}", reviewId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Отзыв с ID=" + reviewId + "не найден");
        }
    }

    private Review mapToReview(ResultSet rs, Integer rowNum) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("reviewId"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("isPositive"))
                .userId(rs.getInt("userId"))
                .filmId(rs.getInt("filmId"))
                .useful(rs.getInt("useful"))
                .build();
    }
}
