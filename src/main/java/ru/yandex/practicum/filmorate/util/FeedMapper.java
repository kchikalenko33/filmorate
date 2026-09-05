package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.FeedDto;

import java.util.List;

@UtilityClass
public class FeedMapper {
    public Feed fromDto(FeedDto feedDto) {
        return Feed.builder()
                .timestamp(feedDto.getTimestamp())
                .userId(feedDto.getUserId())
                .operation(feedDto.getOperation())
                .eventId(feedDto.getEventId())
                .entityId(feedDto.getEntityId())
                .build();
    }

    public FeedDto toDto(Feed feed) {
        return FeedDto.builder()
                .timestamp(feed.getTimestamp())
                .userId(feed.getUserId())
                .eventId(feed.getEventId())
                .operation(feed.getOperation())
                .entityId(feed.getEntityId())
                .build();
    }

    public List<Feed> listFromDto(List<FeedDto> listFeedDto) {
        return listFeedDto.stream()
                .map(FeedMapper::fromDto)
                .toList();
    }

    public List<FeedDto> listToDto(List<Feed> listFeed) {
        return listFeed.stream()
                .map(FeedMapper::toDto)
                .toList();
    }
}
