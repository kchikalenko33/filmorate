package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedDto;
import ru.yandex.practicum.filmorate.model.FilmDto;

import java.util.List;

@UtilityClass
public class FeedMapper {
    public Feed fromDto(FeedDto feedDto) {
        return Feed.builder()
                .timestamp(feedDto.getTimestamp())
                .userId(feedDto.getUserId())
                .eventId(feedDto.getEventId())
                .operation(feedDto.getOperation())
                .eventId(feedDto.getEventId())
                .entityId(feedDto.getEntityId())
                .build();
    }

    public FeedDto toDto(Feed feed) {
        return FeedDto.builder()
                //todo
                .build();
    }

    public List<Feed> listFromDto(List<FeedDto> listFeedDto) {
        return List.of();
    }

    public List<FeedDto> listToDto(List<Feed> listFeed) {
        return List.of();
    }
}
