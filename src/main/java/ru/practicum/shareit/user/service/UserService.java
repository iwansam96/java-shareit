package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.UserDataIsIncorrectException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto user) throws UserDataIsIncorrectException;

    UserDto getById(Long id) throws UserNotFoundException;

    UserDto update(UserDto userDto, Long userId) throws UserNotFoundException;

    void delete(Long id);
}
