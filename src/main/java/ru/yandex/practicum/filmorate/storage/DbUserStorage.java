package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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

        int id = insert.execute(user.toMap());

        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        return null;
    }

    @Override
    public List<User> readAll() {
        String sql = "SELECT * FROM users";
        return List.of();
    }

    @Override
    public User readById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapToUser(rs, rowNum), id);
            log.info("");
            return user;
        } catch (Exception e) {
            log.warn("UserStorage: пользователь не найден, id={}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Пользователь с id=" + id + " не найден");
        }
    }

    @Override
    public void deleteUser(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";

        if(jdbcTemplate.update(sql, id) != 0) {
            log.info("");
        } else {
            log.info("");
        }
    }

    @Override
    public Object addFriend(Integer id, Integer friendId) {
        String sql = "INSERT INTO users (ussr_id, friend_id) VALUES (?, ?)";
        return null;
    }

    @Override
    public Object deleteFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM users where user_id = ? AND friend_id = ?";
        return null;
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
        return List.of();
    }

    @Override
    public List<User> readCommonFriends(Integer id, Integer otherId) {
        String sql = """
                SELECT u.*
                FROM users u
                JOIN friends f1 ON u.id = f1.friend_id AND f1.user_id = ?1
                JOIN friends f2 ON u.id = f2.friend_id AND f2.user_id = ?2
                """;
        return List.of();
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
}
