package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.MpaMapper;

import java.util.List;

@Service
public class MpaService {
    private MpaStorage storage;

    @Autowired
    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }


    public MpaDto read(Integer id) {
        return MpaMapper.toDto(storage.read(id));
    }

    public List<MpaDto> readAll() {
        return MpaMapper.toDtoList(storage.readAll());
    }
}
