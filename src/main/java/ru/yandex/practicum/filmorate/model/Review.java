package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    Integer reviewId;
    String content;
    Boolean isPositive;
    Integer userId;
    Integer filmId;
    Integer useful;

    public Map<String, Object> toMap() {
        return Map.of(
                "content", content == null ? "" : content,
                "isPositive", isPositive,
                "userId", userId,
                "filmId", filmId,
                "useful", useful == null ? 0 : useful
        );
    }
}
