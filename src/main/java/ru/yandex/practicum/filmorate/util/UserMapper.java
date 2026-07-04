package ru.yandex.practicum.filmorate.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Builder
public class UserMapper {
    public static User userFromDto(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .login(userDto.getLogin())
                .name(userDto.getName())
                .birthday(userDto.getBirthday())
                .build();
    }

    public static UserDto userToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    public static List<User> usersFromDto(List<UserDto> usersDto) {
        return usersDto.stream()
                .map(UserMapper::userFromDto)
                .collect(Collectors.toList());
    }

    public static List<UserDto> usersToDto(List<User> users) {
        return users.stream()
                .map(UserMapper::userToDto)
                .collect(Collectors.toList());
    }
}
