package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Feed {
    Long timestamp;
    Integer userId;
    String eventType;
    String operation;
    Integer eventId;
    Integer entityId;

    public Map<String, Object> toMap() {
        return Map.of("timestamp", timestamp,
                "userId", userId,
                "eventType", eventType,
                "operation", operation,
                "eventId", eventId,
                "entityId", entityId
                );
    }
}
