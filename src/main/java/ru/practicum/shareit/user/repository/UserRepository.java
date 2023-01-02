package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User save(User user);

    User getById(Long id);

    User update(User user, Long userId);
    void delete(Long id);
}
