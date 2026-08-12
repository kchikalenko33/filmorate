package ru.yandex.practicum.filmorate.util;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.MpaDto;

import java.util.List;

@UtilityClass
public class MpaMapper {
    public Mpa fromDto(MpaDto mpaDto) {
        return Mpa.builder()
                .id(mpaDto.getId())
                .name(mpaDto.getName())
                .build();
    }

    public MpaDto toDto(Mpa mpa) {
        return MpaDto.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }

    public List<Mpa> fromDtoList(List<MpaDto> mpaDtoList) {
       return mpaDtoList.stream()
                .map(MpaMapper::fromDto)
                .toList();
    }

    public List<MpaDto> toDtoList(List<Mpa> mpaList) {
        return mpaList.stream()
                .map(MpaMapper::toDto)
                .toList();
    }
}
