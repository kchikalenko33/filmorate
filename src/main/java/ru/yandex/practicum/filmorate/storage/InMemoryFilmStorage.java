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
public class InMemoryFilmStorage implements FilmStorage{
    final Map<Integer, Film> films = new HashMap<>();
    Integer idGen = 1;

    @Override
    public Film create(Film film) {
        Validator.validateFilm(film);

        film.setId(idGen);
        films.put(idGen++, film);

        log.info("FilmStorage: фильм создан, id={}, name='{}'", film.getId(), film.getName());

        return film;
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            Validator.validateFilm(film);
            films.put(film.getId(), film);

            log.info("FilmStorage: фильм обновлен, id={}, name='{}'", film.getId(), film.getName());

            return film;
        } else {
            log.warn("FilmStorage: попытка обновить несуществующий фильм, id={}", film.getId());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с id=" + film.getId() + " не найден");
        }
    }

    @Override
    public List<Film> readAll() {
        log.info("FilmStorage: получен список фильмов, количество={}", films.size());

        return new ArrayList<>(films.values());
    }
}
