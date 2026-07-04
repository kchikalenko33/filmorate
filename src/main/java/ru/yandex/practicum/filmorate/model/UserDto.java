package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @Positive
    Integer id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    @Pattern(regexp = "\\S+")
    String login;
    String name;
    @Past
    LocalDate birthday;
}
