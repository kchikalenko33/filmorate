package ru.yandex.practicum.filmorate.service;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.UserMapper.*;

@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public UserDto create(UserDto userDto) {
        return userToDto(inMemoryUserStorage.create(userFromDto(userDto)));
    }

    public @Nullable UserDto update(UserDto userDto) {
        return userToDto(inMemoryUserStorage.update(userFromDto(userDto)));
    }

    public List<UserDto> readAll() {
        return usersToDto(inMemoryUserStorage.readAll());
    }

    public UserDto readById(Integer id) {
        return userToDto(inMemoryUserStorage.readById(id));
    }

    public Object addFriend(Integer id, Integer friendId) {
        return inMemoryUserStorage.addFriend(id, friendId);
    }

    public Object deleteFriend(Integer id, Integer friendId) {
        return inMemoryUserStorage.deleteFriend(id, friendId);
    }

    public List<UserDto> readFriends(Integer id) {
        return usersToDto(inMemoryUserStorage.readFriends(id));
    }

    public List<UserDto> readCommonFriends(Integer id, Integer otherId) {
        return usersToDto(inMemoryUserStorage.readCommonFriends(id, otherId));
    }
}
