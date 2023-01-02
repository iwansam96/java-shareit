package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User save(User user);

    User getById(Long id);

    User update(UserDto userDto, Long userId);

    void delete(Long id);
}
