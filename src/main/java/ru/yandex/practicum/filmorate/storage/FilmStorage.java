package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmStorage {
    final Map<Integer, Film> films = new HashMap<>();
    Integer idGen = 1;

    public Film create(Film film) {
        Validator.validateFilm(film);
        log.info("");
        return films.put(idGen++, film);
    }

    public Film update(Film film) {
        Validator.validateFilm(film);
        log.info("");
        films.put(film.getId(), film);
        return film;
    }

    public List<Film> readAll() {
        log.info("");
        return new ArrayList<>(films.values());
    }
}
