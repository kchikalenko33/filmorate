package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.GenreMapper;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.util.GenreMapper.*;



@Service
public class GenreService {
    private final GenreStorage storage;

    @Autowired
    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public GenreDto read(Integer id) {
        return GenreMapper.toDto(storage.read(id));
    }

    public Set<GenreDto> readAll() {
        return toDtoList(storage.readAll());
    }
}
