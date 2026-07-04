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
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmDto> create(@RequestBody @Valid FilmDto filmDto) {
        log.info("");
        return new ResponseEntity<>(filmService.create(filmDto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@RequestBody @Valid FilmDto filmDto) {
        log.info("");
        return new ResponseEntity<>(filmService.update(filmDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> readAll() {
        log.info("");
        return new ResponseEntity<>(filmService.readAll(), HttpStatus.OK);
    }
}
