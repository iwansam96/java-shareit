package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@RequestBody UserDto user) {
        log.info("POST /users");
        return userService.save(user);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.info("GET /users/{}", userId);
        return userService.getById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH /users");
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
