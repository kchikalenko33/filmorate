package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.util.FilmMapper.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;

    public FilmService(@Qualifier("DbFilm") FilmStorage filmStorage, FeedStorage feedStorage) {
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
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

    public FilmDto addLike(Integer id, Integer userId) {
        FilmDto film = filmToDto(filmStorage.addLike(id, userId));
        Feed feed = Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .eventType("LIKE")
                .operation("ADD")
                .userId(userId)
                .entityId(id)
                .eventId(-1)
                .build();
        feedStorage.create(feed);

        return film;
    }

    public FilmDto deleteLike(Integer id, Integer userId) {
        FilmDto film = filmToDto(filmStorage.deleteLike(id, userId));
        Feed feed = Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .eventType("LIKE")
                .operation("REMOVE")
                .userId(userId)
                .entityId(id)
                .eventId(-1)
                .build();
        feedStorage.create(feed);

        return film;
    }

    public List<FilmDto> readPopular(Integer count) {
        return filmsToDto(filmStorage.readPopular(count));
    }

    public void delete(Integer id) {
        filmStorage.deleteFilm(id);
    }

    public Set<FilmDto> commonFilms(Integer userId, Integer friendId) {
        return new HashSet<>(filmsToDto(new ArrayList<>(filmStorage.commonFilms(userId, friendId))));
    }
}
