package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.FilmMapper.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("DbFilm") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public FilmDto create(FilmDto filmDto) {
        return filmToDto(filmStorage.create(filmFromDto(filmDto)));
    }


    public FilmDto update(FilmDto filmDto) {
        return filmToDto(filmStorage.update(filmFromDto(filmDto)));
    }

    public List<FilmDto> readAll() {
        return filmsToDto(filmStorage.readAll());
    }

    public FilmDto readById(Integer id) {
        return filmToDto(filmStorage.readById(id));
    }

    public Object addLike(Integer id, Integer userId) {
        return filmStorage.addLike(id, userId);
    }

    public Object deleteLike(Integer id, Integer userId) {
        return filmStorage.deleteLike(id, userId);
    }

    public List<FilmDto> readPopular(Integer count) {
        return filmsToDto(filmStorage.readPopular(count));
    }
}
