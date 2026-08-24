package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
    Integer rate;
    Mpa mpa;
    Set<Genre> genres;

    @Builder.Default
    Set<Integer> likes = new HashSet<>();

    public Map<String, Object> toMap() {
        return Map.of("name", name,
                "description", description,
                "releaseDate", releaseDate,
                "duration", duration,
                "rate", rate == null ? 0 : rate,
                "mpa_id", mpa.getId()
                );
    }


}
