package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review create(Review review);

    Review update(Review review);

    Review readById(Integer reviewId);

    void delete(Integer reviewId);

    List<Review> readAllByFilmId(Integer count, Integer filmId);
}
