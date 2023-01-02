package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public User save(User user);
    public User getById(Long id);
    public User update(UserDto userDto, Long userId);
    public void delete(Long id);
}
