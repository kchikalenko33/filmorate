package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmDto> create(@RequestBody @Valid FilmDto filmDto) {
        log.info("POST /films - создание фильма: name='{}', releaseDate={}",
                filmDto.getName(), filmDto.getReleaseDate());

        return new ResponseEntity<>(filmService.create(filmDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@RequestBody @Valid FilmDto filmDto) {
        log.info("PUT /films - обновление фильма, id={}", filmDto.getId());

        return new ResponseEntity<>(filmService.update(filmDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> readAll() {
        log.info("GET /films - запрос списка всех фильмов");

        return new ResponseEntity<>(filmService.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> readById(@PathVariable Integer id) {
        log.info("GET /films/{} - запрос получения фильма", id);

        return new ResponseEntity<>(filmService.readById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("PUT /films/{}/like/{} - проставление лайка фильму", id, userId);

        return new ResponseEntity<>(filmService.addLike(id, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("DELETE /films/{}/like/{} - удаление лайка у фильма", id, userId);

        return new ResponseEntity<>(filmService.deleteLike(id, userId), HttpStatus.OK);
    }

    @GetMapping("popular")
    public ResponseEntity<List<FilmDto>> readPopular(@RequestParam(value = "10") Integer count) {
        log.info("");

        return new ResponseEntity<>(filmService.readPopular(count), HttpStatus.OK);
    }
}
