package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> readAll();

    Film readById(Integer id);

    void deleteFilm(Integer id);

    Object addLike(Integer id, Integer userId);

    Object deleteLike(Integer id, Integer userId);

    List<Film> readPopular(Integer count);

    Set<Film> commonFilms(Integer userId, Integer friendId);
}
