package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@UtilityClass
public class Validator {

    public static void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmException("Дата должна быть не раньше 28.12.1895");
        }
    }
}
