package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.ReviewDto;

import java.util.List;

@UtilityClass
public class ReviewMapper {
    public Review fromDto(ReviewDto reviewDto) {
        return Review.builder()
                .reviewId(reviewDto.getReviewId())
                .content(reviewDto.getContent())
                .isPositive(reviewDto.getIsPositive())
                .userId(reviewDto.getUserId())
                .filmId(reviewDto.getFilmId())
                .useful(reviewDto.getUseful())
                .build();
    }

    public ReviewDto toDto(Review review) {
        return ReviewDto.builder()
                .reviewId(review.getReviewId())
                .content(review.getContent())
                .isPositive(review.getIsPositive())
                .userId(review.getUserId())
                .filmId(review.getFilmId())
                .useful(review.getUseful())
                .build();
    }

    public List<Review> listFromDto(List<ReviewDto> listReviewDto) {
        return listReviewDto.stream()
                .map(ReviewMapper::fromDto)
                .toList();
    }

    public List<ReviewDto> listToDto(List<Review> listReview) {
        return listReview.stream()
                .map(ReviewMapper::toDto)
                .toList();
    }
}
