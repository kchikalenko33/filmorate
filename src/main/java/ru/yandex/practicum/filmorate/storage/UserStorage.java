package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);
    User update(User user);
    List<User> readAll();

    User readById(Integer id);

    Object addFriend(Integer id, Integer friendId);

    Object deleteFriend(Integer id, Integer friendId);

    List<User> readFriends(Integer id);

    List<User> readCommonFriends(Integer id, Integer otherId);

}
