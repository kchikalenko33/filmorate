package ru.yandex.practicum.filmorate.service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.UserMapper.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDto create(UserDto userDto) {
        return userToDto(userStorage.create(userFromDto(userDto)));
    }

    public @Nullable UserDto update(UserDto userDto) {
        return userToDto(userStorage.update(userFromDto(userDto)));
    }

    public List<UserDto> readAll() {
        return usersToDto(userStorage.readAll());
    }

    public UserDto readById(Integer id) {
        return userToDto(userStorage.readById(id));
    }

    public Object addFriend(Integer id, Integer friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public Object deleteFriend(Integer id, Integer friendId) {
        return userStorage.deleteFriend(id, friendId);
    }

    public List<UserDto> readFriends(Integer id) {
        return usersToDto(userStorage.readFriends(id));
    }

    public List<UserDto> readCommonFriends(Integer id, Integer otherId) {
        return usersToDto(userStorage.readCommonFriends(id, otherId));
    }
}
