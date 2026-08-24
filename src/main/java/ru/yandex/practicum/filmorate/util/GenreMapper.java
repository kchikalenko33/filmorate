package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenreDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class GenreMapper {
    public Genre fromDto(GenreDto genreDto) {
        return Genre.builder()
                .id(genreDto.getId())
                .name(genreDto.getName())
                .build();
    }

    public GenreDto toDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Set<GenreDto> toDtoList(Set<Genre> genres) {
        return genres.stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toSet());
    }
}
