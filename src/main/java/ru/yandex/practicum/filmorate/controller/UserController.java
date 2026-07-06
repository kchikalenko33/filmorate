package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto) {
        log.info("POST /users - создание пользователя: email='{}', login='{}'",
                userDto.getEmail(), userDto.getLogin());

        return new ResponseEntity<>(userService.create(userDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody @Valid UserDto userDto) {
        log.info("PUT /users - обновление пользователя, id={}", userDto.getId());

        return new ResponseEntity<>(userService.update(userDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> readAll() {
        log.info("GET /users - запрос списка всех пользователей");

        return new ResponseEntity<>(userService.readAll(), HttpStatus.OK);
    }
}
