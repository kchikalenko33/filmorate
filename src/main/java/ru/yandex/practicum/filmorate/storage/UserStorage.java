package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validator.prepareUser;

@Slf4j
@Component
public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idGen = 1;

    public User create(User user) {
        prepareUser(user);

        user.setId(idGen);
        users.put(idGen++, user);

        log.info("UserStorage: пользователь создан, id={}, email='{}', login='{}'",
                user.getId(), user.getEmail(), user.getLogin());

        return user;
    }

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

    public List<User> readAll() {
        log.info("UserStorage: получен список пользователей, количество={}", users.size());

        return new ArrayList<>(users.values());
    }
}
