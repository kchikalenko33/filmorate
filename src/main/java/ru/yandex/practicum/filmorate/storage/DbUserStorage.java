package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component("DbUser")
public class DbUserStorage implements UserStorage {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        int id = insert.executeAndReturnKey(user.toMap()).intValue();

        user.setId(id);
        log.info("DbUserStorage: пользователь успешно создан, ID={}, с именем={}", id, user.getName());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        int count = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());

        if (count > 0) {
            log.info("DbUserStorage: user с id = {} обновлен", user.getId());
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "DbUserStorage: user с id =" + user.getId() +
                    "не обновлен");
        }
    }

    @Override
    public List<User> readAll() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, this::mapToUser);
        log.info("DbUserStorage: получено {} пользователей", users.size());
        return users;
    }

    @Override
    public User readById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapToUser(rs, rowNum), id);
            log.info("DbUserStorage: пользователь найден, ID={}, имя={}", id, user.getName());
            return user;
        } catch (Exception e) {
            log.warn("DbUserStorage: пользователь не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Пользователь с id=" + id + " не найден");
        }
    }

    @Override
    public void deleteUser(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";

        if (jdbcTemplate.update(sql, id) != 0) {
            log.info("DbUserStorage: пользователь с ID={} удален", id);
        } else {
            log.info("DbUserStorage: пользователь с ID={} не был удален, т.к его не существует", id);
        }
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        readById(id);
        readById(friendId);

//        if (id.equals(friendId)) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Нельзя добавить самого себя в друзья");
//        }

        String checkSql = "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id, friendId);

        if (count != null && count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Пользователи уже являются друзьями");
        }

        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, friendId);

        User user = readById(id);

        log.info("DbUserStorage: пользователь id={} добавил в друзья пользователя id={}", id, friendId);
        return user;
    }

    @Override
    public User deleteFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM friends where user_id = ? AND friend_id = ?";

        readById(id);
        readById(friendId);

        if (jdbcTemplate.update(sql, id, friendId) == 0) {
            log.info("DbUserStorage: друзья не удалены");
        } else {
            log.info("DbUserStorage: пользователи с ID={} и ID={} больше не друзья", id, friendId);
        }
        return readById(id);
    }

    @Override
    public List<User> readFriends(Integer id) {
        String sql = """
                SELECT u.*
                FROM users u
                JOIN friends f
                ON u.id = f.friend_id
                WHERE f.user_id = ?
                """;
        readById(id);
        List<User> friends = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = mapToUser(rs, rowNum);
            user.setFriends(getFriendsForUser(user.getId()));
            return user;
        }, id);
        log.info("DbUserStorage: получен список друзей, пользователя с ID={}", id);
        return friends;
    }

    @Override
    public List<User> readCommonFriends(Integer id, Integer otherId) {
        String sql = """
                SELECT u.*
                FROM users u
                JOIN friends f1 ON u.id = f1.friend_id AND f1.user_id = ?1
                JOIN friends f2 ON u.id = f2.friend_id AND f2.user_id = ?2
                """;
        List<User> users = jdbcTemplate.query(sql, this::mapToUser, id, otherId);
        log.info("DbUserStorage: получен список общих друзей для пользователей с ID={} и с ID={}", id, otherId);
        return users;
    }

    private User mapToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    private Set<User> getFriendsForUser(Integer userId) {
        String sql = """
            SELECT u.*
            FROM users u
            JOIN friends f ON u.id = f.friend_id
            WHERE f.user_id = ?
            """;
        return new HashSet<>(jdbcTemplate.query(sql, this::mapToUser, userId));
    }

}
