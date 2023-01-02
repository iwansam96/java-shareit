package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryUserRepository implements UserRepository {
    List<User> users = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public User save(User user) {
        User userWithSameEmail = users.stream()
                .filter(nextUser -> nextUser.getEmail().equals(user.getEmail()))
                .findAny()
                .orElse(null);
        if (userWithSameEmail != null)
            return null;
        user.setId(IdGenerator.generateId("user"));
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
    public User update(User user, Long userId) {
        User oldUser = users.stream()
                .filter(nextUser -> Objects.equals(nextUser.getId(), userId))
                .findFirst()
                .orElse(null);
        boolean isNonUnique = users.stream()
                .anyMatch(nextUser -> nextUser.getEmail().equals(user.getEmail()));
        if (isNonUnique)
            return null;
        if (oldUser != null) {
            user.setId(userId);
            if (user.getName() == null)
                user.setName(oldUser.getName());
            if (user.getEmail() == null)
                user.setEmail(oldUser.getEmail());
            users.remove(oldUser);
            users.add(user);
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        users.stream()
                .filter(nextUser -> Objects.equals(nextUser.getId(), id))
                .findFirst().ifPresent(userToDelete -> users.remove(userToDelete));
    }
}
