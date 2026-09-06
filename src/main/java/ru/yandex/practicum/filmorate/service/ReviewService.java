package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.ReviewDto;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.util.ReviewMapper;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public ReviewDto create(ReviewDto reviewDto) {
        return ReviewMapper.toDto(reviewStorage.create(ReviewMapper.fromDto(reviewDto)));
    }

    public ReviewDto update(ReviewDto reviewDto) {
        return ReviewMapper.toDto(reviewStorage.update(ReviewMapper.fromDto(reviewDto)));
    }
}
