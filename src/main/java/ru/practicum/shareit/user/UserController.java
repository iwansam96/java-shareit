package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
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
    public List<User> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @PostMapping
    public User save(@Valid @RequestBody User user) {
        log.info("POST /users");
        if (user == null) {
            log.error("user is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User newUser = userService.save(user);
        if (newUser == null) {
            log.error("user with the same email already exists");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return newUser;
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        log.info("GET /users/{}", userId);
        if (userId == null) {
            log.error("userId is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User userById = userService.getById(userId);
        if (userById == null) {
            log.error("userId not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userById;
    }

    @PatchMapping("/{userId}")
    public User update(@Valid @RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH /users");
        if (userDto == null) {
            log.error("userDto is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        User updatedUser = userService.update(userDto, userId);
        if (updatedUser == null) {
            log.error("updatedUser is null");
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return updatedUser;
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
