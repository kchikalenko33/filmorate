package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.FilmMapper.*;

@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public FilmDto create(FilmDto filmDto) {
        return filmToDto(inMemoryFilmStorage.create(filmFromDto(filmDto)));
    }


    public FilmDto update(FilmDto filmDto) {
        return filmToDto(inMemoryFilmStorage.update(filmFromDto(filmDto)));
    }

    public List<FilmDto> readAll() {
        return filmsToDto(inMemoryFilmStorage.readAll());
    }

    public FilmDto readById(Integer id) {
        return filmToDto(inMemoryFilmStorage.readById(id));
    }

    public Object addLike(Integer id, Integer userId) {
        return inMemoryFilmStorage.addLike(id, userId);
    }

    public Object deleteLike(Integer id, Integer userId) {
        return inMemoryFilmStorage.deleteLike(id, userId);
    }

    public List<FilmDto> readPopular(Integer count) {
        return filmsToDto(inMemoryFilmStorage.readPopular(count));
    }
}
