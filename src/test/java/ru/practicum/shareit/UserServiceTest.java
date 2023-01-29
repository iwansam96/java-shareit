package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.UserDataIsIncorrectException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceTest {
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    public void prepareMocks() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void shouldReturnEmptyListWhenGetAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        var expected = new ArrayList<UserDto>();
        var actual = userService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnListWithOneUserWhenGetAllUsers() {
        var user = new User();
        var expected = new ArrayList<User>();
        expected.add(user);
        Mockito.when(userRepository.findAll()).thenReturn(expected);

        var actual = userService.getAll();
        Assertions.assertEquals(expected.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()), actual);
    }

    @Test
    public void shouldReturnListWithTwoUsersWhenGetAllUsers() {
        var user1 = new User();
        user1.setName("user1");
        var user2 = new User();
        user2.setName("user2");
        var expected = new ArrayList<User>();
        expected.add(user1);
        expected.add(user2);
        Mockito.when(userRepository.findAll()).thenReturn(expected);

        var actual = userService.getAll();
        Assertions.assertEquals(expected.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList()), actual);
    }

    @Test
    public void shouldThrowUserDataIsIncorrectExceptionWhenSaveNullUser() {
        Assertions.assertThrows(UserDataIsIncorrectException.class, () -> userService.save(null));
    }

    @Test
    public void shouldThrowUserDataIsIncorrectExceptionWhenSaveUserWithoutEmail() {
        var user1 = new User();
        user1.setName("user1");
        Assertions.assertThrows(UserDataIsIncorrectException.class,
                () -> userService.save(UserMapper.toUserDto(user1)));
    }

    @Test
    public void shouldReturnNewUserWhenSave() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);

        var actual = userService.save(UserMapper.toUserDto(user1));
        Assertions.assertEquals(UserMapper.toUserDto(user1), actual);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenGetByIdWithIncorrectId() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    public void shouldReturnOneUserWhenGetById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        var actual = userService.getById(1L);
        Assertions.assertEquals(UserMapper.toUserDto(user1), actual);
    }

    @Test
    public void shouldThrowUserDataIsIncorrectExceptionWhenUpdateNullUser() {
        Assertions.assertThrows(UserDataIsIncorrectException.class, () -> userService.update(null, 1L));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenUpdateWithIncorrectId() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.update(UserMapper.toUserDto(user1), 99L));
    }

    @Test
    public void shouldReturnUpdatedUserWhenUpdate() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");

        var user1Updated = new User();
        user1.setName("user1_updated");
        user1.setEmail("user1_updated@email.com");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1Updated);

        var actual = userService.update(UserMapper.toUserDto(user1Updated), 1L);
        Assertions.assertEquals(UserMapper.toUserDto(user1Updated), actual);
    }

}
