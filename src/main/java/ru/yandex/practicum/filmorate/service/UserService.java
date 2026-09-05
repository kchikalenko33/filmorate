package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedDto;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.List;

import static ru.yandex.practicum.filmorate.util.UserMapper.*;
import static ru.yandex.practicum.filmorate.util.FeedMapper.*;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FeedStorage feedStorage;

    @Autowired
    public UserService(@Qualifier("DbUser") UserStorage userStorage, FeedStorage feedStorage) {
        this.userStorage = userStorage;
        this.feedStorage = feedStorage;
    }

    public UserDto create(UserDto userDto) {
        return userToDto(userStorage.create(userFromDto(userDto)));
    }

    public UserDto update(UserDto userDto) {
        return userToDto(userStorage.update(userFromDto(userDto)));
    }

    public List<UserDto> readAll() {
        return usersToDto(userStorage.readAll());
    }

    public UserDto readById(Integer id) {
        return userToDto(userStorage.readById(id));
    }

    public void delete(Integer id) {
        userStorage.deleteUser(id);
    }

    public UserDto addFriend(Integer id, Integer friendId) {
        UserDto user = userToDto(userStorage.addFriend(id, friendId));
        Feed feed = new Feed(
                Instant.now().toEpochMilli(),
                id,
                "FRIEND",
                "ADD",
                -1,
                friendId
        );
        feedStorage.create(feed);
        return user;
    }

    public UserDto deleteFriend(Integer id, Integer friendId) {
        UserDto user = userToDto(userStorage.deleteFriend(id, friendId));
        Feed feed = new Feed(
                Instant.now().toEpochMilli(),
                id,
                "FRIEND",
                "REMOVE",
                -1,
                friendId
        );
        feedStorage.create(feed);
        return user;
    }

    public List<UserDto> readFriends(Integer id) {
        return usersToDto(userStorage.readFriends(id));
    }

    public List<UserDto> readCommonFriends(Integer id, Integer otherId) {
        return usersToDto(userStorage.readCommonFriends(id, otherId));
    }

    public List<FeedDto> readFeed(Integer id) {
        return listToDto(feedStorage.readFeed(id));
    }
}
