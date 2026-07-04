package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
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
            @NotBlank
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    Integer duration;
    @Positive
    Integer rate;
}
