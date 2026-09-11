package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.ReviewDto;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

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
    public ResponseEntity<ReviewDto> create(@RequestBody @Valid ReviewDto review) {
        log.info("POST /reviews: Создание отзыва пользователя c ID={} для фильма c ID={}", review.getUserId(),
                review.getFilmId());

        return new ResponseEntity<>(reviewService.create(review), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ReviewDto> update(@RequestBody @Valid ReviewDto reviewDto) {
        log.info("PUT /reviews: Обновление ");

        return new ResponseEntity<>(reviewService.update(reviewDto), HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> readById(@PathVariable Integer reviewId) {
        log.info("GET reviews/{} - получение отзыва", reviewId);

        return new ResponseEntity<>(reviewService.readById(reviewId), HttpStatus.OK);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Integer reviewId) {
        log.info("DELETE reviews/{} - удаление отзыва", reviewId);
        reviewService.delete(reviewId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ReviewDto>> readAllByFilmId(@RequestParam(defaultValue = "10", required = false)
                                                               Integer count, Integer filmId) {
        log.info("GET reviews/filmId={}", filmId);
        return new ResponseEntity<>(reviewService.readAllByFilmId(count, filmId), HttpStatus.OK);
    }
}
