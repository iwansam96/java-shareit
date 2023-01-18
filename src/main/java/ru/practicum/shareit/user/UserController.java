package ru.practicum.shareit.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    @NonNull
    private UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info("GET /users");
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@Valid @NotNull @RequestBody UserDto user) {
        log.info("POST /users");
        return userService.save(user);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable @Valid @NotNull Long userId) {
        log.info("GET /users/{}", userId);
        return userService.getById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@Valid @NotNull @RequestBody UserDto userDto, @Valid @NotNull @PathVariable Long userId) {
        log.info("PATCH /users");
        return userService.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable @Valid @NotNull Long userId) {
        log.info("DELETE /users/{}", userId);
        userService.delete(userId);
    }
}
