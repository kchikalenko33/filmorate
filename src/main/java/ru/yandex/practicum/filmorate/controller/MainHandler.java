package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ErrorInfo;
import ru.yandex.practicum.filmorate.exception.FilmException;

import java.time.LocalDate;

@RestControllerAdvice
public class MainHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorInfo> handleResponseStatusException(ResponseStatusException ex) {
        ErrorInfo error = new ErrorInfo(ex.getMessage(), ex.getClass().getName(), LocalDate.now());

        return new ResponseEntity<>(error,ex.getStatusCode());
    }

    @ExceptionHandler(FilmException.class)
    public ResponseEntity<ErrorInfo> handleFilmException(FilmException ex) {
        ErrorInfo error = new ErrorInfo(ex.getMessage(), ex.getClass().getName(), LocalDate.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
