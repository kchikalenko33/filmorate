package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import static ru.yandex.practicum.filmorate.util.Validator.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer idGen = 1;

    public User create(User user) {
        prepareUser(user);
        log.info("");
        user.setId(idGen++);
        users.put(idGen, user);
        return user;
    }

    public User update(User user) {
        if (users.containsKey(user.getId())) {
            prepareUser(user);
            log.info("");
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
