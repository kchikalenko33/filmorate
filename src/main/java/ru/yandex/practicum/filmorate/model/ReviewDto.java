package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewDto {
    Integer reviewId;
    String content;
    Boolean isPositive;
    Integer userId;
    Integer filmId;
    Integer useful;
}
