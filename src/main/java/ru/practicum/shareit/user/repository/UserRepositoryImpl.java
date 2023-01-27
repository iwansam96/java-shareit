package ru.practicum.shareit.user.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.user.model.User;

public class UserRepositoryImpl implements UserRepositoryCustom {
    private final UserRepository userRepository;

    public UserRepositoryImpl(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findAll().stream()
                .filter(nextUser -> nextUser.getEmail().equals(email))
                .findAny()
                .orElse(null);
    }
}
