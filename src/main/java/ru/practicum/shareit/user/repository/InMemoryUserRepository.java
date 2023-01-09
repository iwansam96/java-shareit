package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.IdGenerator;
import ru.practicum.shareit.util.IdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private List<User> users = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User save(User user) {
        user.setId(IdGenerator.generateId(IdType.USER));
        users.add(user);
        return user;
    }

    @Override
    public User getById(Long id) {
        return users.stream()
                .filter(nextUser -> Objects.equals(nextUser.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return users.stream()
                .filter(nextUser -> nextUser.getEmail().equals(email))
                .findAny()
                .orElse(null);
    }

    @Override
    public User update(User user, Long userId) {
        delete(userId);
        users.add(user);
        return user;
    }

    @Override
    public void delete(Long id) {
        users.stream()
                .filter(nextUser -> Objects.equals(nextUser.getId(), id))
                .findFirst().ifPresent(userToDelete -> users.remove(userToDelete));
    }
}
