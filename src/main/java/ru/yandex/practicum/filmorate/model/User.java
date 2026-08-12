package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class User {
    Integer id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    @Builder.Default
    Set<Integer> friends = new HashSet<>();

    public Map<String, Object> toMap() {
        return Map.of("email", email,
                "login", login,
                "name", name,
                "birthday", birthday);
    }
}
