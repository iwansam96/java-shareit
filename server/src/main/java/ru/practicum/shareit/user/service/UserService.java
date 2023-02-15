package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto user);

    UserDto getById(Long id);

    UserDto update(UserDto userDto, Long userId);

    void delete(Long id);
}
