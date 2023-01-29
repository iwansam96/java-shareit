package ru.practicum.shareit.user.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.exception.UserDataIsIncorrectException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    @NonNull
    private UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) {
        if (userDto == null)
            throw new UserDataIsIncorrectException("user data is incorrect");
        User user = UserMapper.toUser(userDto);
        if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserDataIsIncorrectException("user data is incorrect");
        }
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("user with id " + id + " not found");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        if (userDto == null)
            throw new UserDataIsIncorrectException("user data is incorrect");
        User oldUser = userRepository.findById(userId).orElse(null);
        User newUser = UserMapper.toUser(userDto);
        if (oldUser == null)
            throw new UserNotFoundException("user with id " + userId + " not found");
        newUser.setId(userId);
        if (newUser.getName() == null)
            newUser.setName(oldUser.getName());
        if (newUser.getEmail() == null)
            newUser.setEmail(oldUser.getEmail());
        User updatedUser = userRepository.save(newUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void delete(Long id) {
        User userToDelete = userRepository.findById(id).orElse(null);
        if (userToDelete == null)
            throw new UserNotFoundException("user " + id + " not found");
        userRepository.delete(userToDelete);
    }
}
