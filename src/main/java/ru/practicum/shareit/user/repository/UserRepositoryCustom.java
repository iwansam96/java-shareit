package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

public interface UserRepositoryCustom {
    User getByEmail(String email);
}
