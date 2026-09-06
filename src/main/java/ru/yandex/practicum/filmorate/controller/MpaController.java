package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("{id}")
    public ResponseEntity<MpaDto> read(@PathVariable Integer id) {
        log.info("GET /mpa/{} - запрос получения рейтинга", id);
        return new  ResponseEntity<>(mpaService.read(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MpaDto>> readAll() {
        log.info("GET /mpa - запрос получения всех рейтингов");
        return new ResponseEntity<>(mpaService.readAll(), HttpStatus.OK);
    }
}
