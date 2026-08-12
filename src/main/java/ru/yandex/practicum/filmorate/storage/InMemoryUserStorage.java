package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

import static ru.yandex.practicum.filmorate.util.Validator.prepareUser;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idGen = 1;

    @Override
    public User create(User user) {
        prepareUser(user);

        user.setId(idGen);
        users.put(idGen++, user);

        log.info("UserStorage: пользователь создан, id={}, email='{}', login='{}'",
                user.getId(), user.getEmail(), user.getLogin());

        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            prepareUser(user);

            users.put(user.getId(), user);

            log.info("UserStorage: пользователь обновлен, id={}, email='{}', login='{}'",
                    user.getId(), user.getEmail(), user.getLogin());

            return user;
        } else {
            log.warn("UserStorage: попытка обновить несуществующего пользователя, id={}", user.getId());

            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Пользователь с id=" + user.getId() + " не найден");
        }
    }

    @Override
    public List<User> readAll() {
        log.info("UserStorage: получен список пользователей, количество={}", users.size());

        return new ArrayList<>(users.values());
    }

    @Override
    public User readById(Integer id) {
        User user = users.get(id);
        if (user == null) {
            log.warn("UserStorage: пользователь не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    @Override
    public void deleteUser(Integer id) {

    }

    @Override
    public Object addFriend(Integer id, Integer friendId) {
        User user = readById(id);
        User friend = readById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("UserStorage: пользователь id={} добавил в друзья пользователя id={}", id, friendId);

        return Map.of("result", "ok");
    }

    @Override
    public Object deleteFriend(Integer id, Integer friendId) {
        User user = readById(id);
        User friend = readById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        log.info("UserStorage: пользователь id={} удалил из друзей пользователя id={}", id, friendId);

        return Map.of("result", "ok");
    }

    @Override
    public List<User> readFriends(Integer id) {
        User user = readById(id);
        List<User> friends = new ArrayList<>();

        user.getFriends().forEach(i -> friends.add(readById(i)));

        log.info("UserStorage: получен список друзей пользователя id={}, количество={}",
                id, friends.size());

        return friends;
    }

    @Override
    public List<User> readCommonFriends(Integer id, Integer otherId) {
        User user = readById(id);
        User otherUser = readById(otherId);

        Set<Integer> temp = new HashSet<>(otherUser.getFriends());
        Set<Integer> commonFriendsIds = new HashSet<>();

        for (int value : user.getFriends()) {
            if (temp.contains(value)) commonFriendsIds.add(value);
        }

        List<User> commonFriends = new ArrayList<>();
        commonFriendsIds.forEach(i -> commonFriends.add(readById(i)));

        log.info("UserStorage: получен список общих друзей пользователя id={} с другим пользователем otherId={}, " +
                        "количество={}", id, otherId, commonFriends.size());

        return commonFriends;
    }
}
