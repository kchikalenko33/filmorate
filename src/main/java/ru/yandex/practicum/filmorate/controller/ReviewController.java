package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

@RestController
@RequestMapping("/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewDto> create(@RequestBody ReviewDto review) {
        log.info("POST /reviews: Создание отзыва пользователя c ID={} для фильма c ID={}", review.getUserId(),
                review.getFilmId());

        return new ResponseEntity<>(reviewService.create(review), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> update(@RequestBody ReviewDto reviewDto) {
        log.info("PUT /reviews: Обновление ");

        return new ResponseEntity<>(reviewService.update(reviewDto), HttpStatus.OK);
    }
}
