package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDto {
    @Positive
    Integer id;
    @NotNull
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    Integer duration;
    @Positive
    Integer rate;
}
