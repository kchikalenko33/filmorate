package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Component
public class DbMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa read(Integer id) {
        String sql = "SELECT * FROM Mpa WHERE id = ?";
        Mpa mpa = jdbcTemplate.queryForObject(sql, this::mapToMpa);
        log.info("");
        return mpa;
    }

    private Mpa mapToMpa(ResultSet rs, Integer rowNum) {
        try {
            return Mpa.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .build();
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
