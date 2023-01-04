package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.IncorrectUserDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserWithSameEmailException;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto save(UserDto user) throws IncorrectUserDataException, UserWithSameEmailException;

    UserDto getById(Long id) throws UserNotFoundException;

    UserDto update(UserDto userDto, Long userId) throws UserWithSameEmailException, UserNotFoundException;

    void delete(Long id);
}
