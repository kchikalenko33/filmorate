package ru.yandex.practicum.filmorate.util;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmDto;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class FilmMapper {
    public Film filmFromDto(FilmDto filmDto) {
        return Film.builder()
                .id(filmDto.getId())
                .name(filmDto.getName())
                .description(filmDto.getDescription())
                .releaseDate(filmDto.getReleaseDate())
                .duration(filmDto.getDuration())
                .mpa(filmDto.getMpa())
                .likes(filmDto.getLikes() == null ? new HashSet<>() : filmDto.getLikes())
                .build();
    }

    public FilmDto filmToDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(film.getMpa())
                .likes(film.getLikes() == null ? new HashSet<>() : film.getLikes())
                .build();
    }

    public List<Film> filmsFromDto(List<FilmDto> filmsDto) {
        return filmsDto.stream()
                .map(FilmMapper::filmFromDto)
                .collect(Collectors.toList());
    }

    public List<FilmDto> filmsToDto(List<Film> films) {
        return films.stream()
                .map(FilmMapper::filmToDto)
                .collect(Collectors.toList());
    }
}
