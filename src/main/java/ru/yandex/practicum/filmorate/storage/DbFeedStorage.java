package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Feed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class DbFeedStorage implements FeedStorage {
    private static final Logger log = LoggerFactory.getLogger(DbFeedStorage.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFeedStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Feed create(Feed feed) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("feeds")
                .usingGeneratedKeyColumns("event_id");
        Map<String, Object> map = feed.toMap();
        Integer eventId = insert.executeAndReturnKey(map).intValue();
        feed.setEventId(eventId);
        return feed;
    }

    @Override
    public List<Feed> readFeed(Integer id) {
        String sql = """
                SELECT *
                FROM feeds
                WHERE userId = ?
                """;

        List<Feed> feeds = jdbcTemplate.query(sql, this::mapToFeed, id);
        log.info("");
        return feeds;
    }

    private Feed mapToFeed(ResultSet rs, int rowNum) {
        try {
            return Feed.builder()
                    .timestamp(rs.getLong("timestamp"))
                    .eventType(rs.getString("eventType"))
                    .operation(rs.getString("operation"))
                    .eventId(rs.getInt("event_id"))
                    .userId(rs.getInt("userId"))
                    .entityId(rs.getInt("entityId"))
                    .build();
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
