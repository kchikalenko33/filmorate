package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    String content;
    @NotNull
    Boolean isPositive;
    Integer userId;
    Integer filmId;
    Integer useful;
}
