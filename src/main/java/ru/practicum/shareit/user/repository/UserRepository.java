package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    public List<User> getAll();
    public User save(User user);
    public User getById(Long id);
    public User update(User user, Long userId);
    public void delete(Long id);
}
