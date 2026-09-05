package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {
    Feed create(Feed feed);

    List<Feed> readFeed(Integer id);
}
