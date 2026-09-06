package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Set;

@RestController
@Slf4j
@RequestMapping("genres")
public class GenreController {
    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("{id}")
    public ResponseEntity<GenreDto> read(@PathVariable Integer id) {
        log.info("GET /genres/{} - запрос получения жанра", id);
        return new ResponseEntity<>(genreService.read(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<GenreDto>> readAll() {
        log.info("GET /genres - запрос получения всех жанров");
        return new ResponseEntity<>(genreService.readAll(), HttpStatus.OK);
    }
}
