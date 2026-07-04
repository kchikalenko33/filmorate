package ru.yandex.practicum.filmorate.service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import static ru.yandex.practicum.filmorate.util.UserMapper.userFromDto;
import static ru.yandex.practicum.filmorate.util.UserMapper.userToDto;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(UserDto userDto) {
        return userToDto(userStorage.create(userFromDto(userDto)));
    }

    public @Nullable UserDto update(UserDto userDto) {
        return userToDto(userStorage.update(userFromDto(userDto)));
    }
}
