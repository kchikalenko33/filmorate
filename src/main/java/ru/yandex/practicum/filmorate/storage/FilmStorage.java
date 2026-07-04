package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
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
        film.setId(idGen);
        films.put(idGen++, film);
        return film;
    }

    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            Validator.validateFilm(film);
            log.info("");
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<Film> readAll() {
        log.info("");
        return new ArrayList<>(films.values());
    }
}
