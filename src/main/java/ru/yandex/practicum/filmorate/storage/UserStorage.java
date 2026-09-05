package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDto;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    List<User> readAll();

    User readById(Integer id);

    void deleteUser(Integer id);

    User addFriend(Integer id, Integer friendId);

    User deleteFriend(Integer id, Integer friendId);

    List<User> readFriends(Integer id);

    List<User> readCommonFriends(Integer id, Integer otherId);

}
