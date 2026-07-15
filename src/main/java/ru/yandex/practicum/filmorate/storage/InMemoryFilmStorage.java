package ru.yandex.practicum.filmorate.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.Validator;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InMemoryFilmStorage implements FilmStorage {
    final Map<Integer, Film> films = new HashMap<>();
    UserStorage userStorage;
    Integer idGen = 1;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

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

    @Override
    public Film readById(Integer id) {
        Film film = films.get(id);
        if (film == null) {
            log.warn("FilmStorage: фильм не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Фильм с id=" + id + " не найден");
        }
        return film;
    }

    @Override
    public Object addLike(Integer id, Integer userId) {
        Film film = readById(id);
        userStorage.readById(userId);

        film.getLikes().add(userId);

        log.info("FilmStorage: фильму id={} добавлен лайк от пользователя id={}", id, userId);

        return Map.of("res", "ok");
    }

    @Override
    public Object deleteLike(Integer id, Integer userId) {
        Film film = readById(id);
        userStorage.readById(userId);

        film.getLikes().remove(userId);

        log.info(" ");

        return Map.of("res", "ok");
    }

    @Override
    public List<Film> readPopular(Integer count) {
        log.info("");
        return films.values().stream()
                .sorted(((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
