package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

public class UserServiceUnitTest {
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    public void prepareMocks() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Test getAll method (empty list)")
    public void shouldReturnEmptyListWhenGetAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(new ArrayList<>());

        var expected = new ArrayList<UserDto>();
        var actual = userService.getAll();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test getAll method (1 user)")
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
    @DisplayName("Test getAll method (2 users)")
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
    @DisplayName("Test save method throws UserDataIsIncorrectException (user is null)")
    public void shouldThrowUserDataIsIncorrectExceptionWhenSaveNullUser() {
        Assertions.assertThrows(UserDataIsIncorrectException.class, () -> userService.save(null));
    }

    @Test
    @DisplayName("Test save method throws UserDataIsIncorrectException (user without email)")
    public void shouldThrowUserDataIsIncorrectExceptionWhenSaveUserWithoutEmail() {
        var user1 = new User();
        user1.setName("user1");
        Assertions.assertThrows(UserDataIsIncorrectException.class,
                () -> userService.save(UserMapper.toUserDto(user1)));
    }

    @Test
    @DisplayName("Test save method")
    public void shouldReturnNewUserWhenSave() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);

        var actual = userService.save(UserMapper.toUserDto(user1));
        Assertions.assertEquals(UserMapper.toUserDto(user1), actual);
    }

    @Test
    @DisplayName("Test getById method throws UserNotFoundException (incorrect id)")
    public void shouldThrowUserNotFoundExceptionWhenGetByIdWithIncorrectId() {
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    @DisplayName("Test getById method")
    public void shouldReturnOneUserWhenGetById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        var actual = userService.getById(1L);
        Assertions.assertEquals(UserMapper.toUserDto(user1), actual);
    }

    @Test
    @DisplayName("Test update method throws UserDataIsIncorrectException")
    public void shouldThrowUserDataIsIncorrectExceptionWhenUpdateNullUser() {
        Assertions.assertThrows(UserDataIsIncorrectException.class, () -> userService.update(null, 1L));
    }

    @Test
    @DisplayName("Test update method throws UserNotFoundException")
    public void shouldThrowUserNotFoundExceptionWhenUpdateWithIncorrectId() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.update(UserMapper.toUserDto(user1), 99L));
    }

    @Test
    @DisplayName("Test update method")
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
