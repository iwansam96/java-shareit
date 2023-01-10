package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.exception.IncorrectUserDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserWithSameEmailException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto save(UserDto userDto) throws IncorrectUserDataException, UserWithSameEmailException {
        User user = UserMapper.toUser(userDto);
        if (user.getName() == null || user.getName().isBlank() || user.getEmail() == null || user.getEmail().isBlank())
            throw new IncorrectUserDataException("user name is incorrect");
        User newUser = userRepository.save(user);
        return UserMapper.toUserDto(newUser);
    }

    @Override
    public UserDto getById(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("user with id " + id + "not found");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) throws UserWithSameEmailException, UserNotFoundException {
        User oldUser = userRepository.findById(userId).orElse(null);
        User newUser = UserMapper.toUser(userDto);
        if (oldUser == null)
            throw new UserNotFoundException("user with id " + userId + " not found");
        boolean isEmailUpdating = !oldUser.getEmail().equals(newUser.getEmail());
        if (isEmailUpdating) {
            User userWithSameEmail = userRepository.getByEmail(newUser.getEmail());
            if (userWithSameEmail != null)
                throw new UserWithSameEmailException("email is not unique");
        }
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
        userRepository.delete(userRepository.findById(id).get());
    }
}
