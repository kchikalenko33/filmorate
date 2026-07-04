package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<Film> create(@RequestBody Film film) {
        log.info("");
        return new ResponseEntity<>(filmService.create(film), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Film> update(@RequestBody Film film) {
        log.info("");
        return new ResponseEntity<>(filmService.update(film), HttpStatus.OK);
    }

    @GetMapping
    public  ResponseEntity<List<Film>> readAll() {
        log.info("");
        return  new ResponseEntity<>(filmService.readAll(), HttpStatus.OK);
    }
}
