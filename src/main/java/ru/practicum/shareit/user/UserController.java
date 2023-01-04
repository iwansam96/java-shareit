package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.IncorrectUserDataException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserWithSameEmailException;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@Valid @RequestBody UserDto user) {
        log.info("POST /users");
        if (user == null) {
            log.error("user is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            return userService.save(user);
        } catch (IncorrectUserDataException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (UserWithSameEmailException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("GET /users/{}", userId);
        if (userId == null) {
            log.error("userId is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            return userService.getById(userId);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH /users");
        if (userDto == null) {
            log.error("userDto is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            return userService.update(userDto, userId);
        } catch (UserWithSameEmailException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("DELETE /users/{}", userId);
        if (userId == null) {
            log.error("userId is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        userService.delete(userId);
    }
}
