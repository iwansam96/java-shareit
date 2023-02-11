package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    @DisplayName("Test save method")
    public void save() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");

        UserDto userSaved = userService.save(UserMapper.toUserDto(user1));

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        User actual = query.getSingleResult();
        Assertions.assertEquals(userSaved, UserMapper.toUserDto(actual));
    }

    @Test
    @DisplayName("Test getAll method")
    public void getAll() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        userService.save(UserMapper.toUserDto(user2));

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> userSaved = query.getResultList();

        List<UserDto> allUsers = userService.getAll();

        Assertions.assertEquals(userSaved.stream().map(UserMapper::toUserDto).collect(Collectors.toList()), allUsers);
    }

    @Test
    @DisplayName("Test getById method")
    public void getById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        userService.save(UserMapper.toUserDto(user1));

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        User userSaved = query.getSingleResult();

        UserDto userById = userService.getById(userSaved.getId());

        Assertions.assertEquals(UserMapper.toUserDto(userSaved), userById);
    }

    @Test
    @DisplayName("Test update method")
    public void update() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        userService.save(UserMapper.toUserDto(user1));

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        User oldUser = query.getSingleResult();

        user1.setEmail("updated@updail.em");
        userService.update(UserMapper.toUserDto(user1), oldUser.getId());

        User newUser = query.getSingleResult();

        Assertions.assertEquals(oldUser, newUser);
    }

    @Test
    @DisplayName("Test delete method")
    public void delete() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        var user1Saved = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        var user2Saved = userService.save(UserMapper.toUserDto(user2));

        TypedQuery<User> query = em.createQuery("select u from User u", User.class);
        List<User> oldUsers = query.getResultList();
        oldUsers.remove(0);

        userService.delete(user1Saved.getId());

        List<User> newUsers = query.getResultList();

        Assertions.assertEquals(oldUsers, newUsers);
    }
}
