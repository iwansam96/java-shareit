package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Get all users");
        return userClient.getAll();
    }

    @PostMapping
    public ResponseEntity<Object> save(@Valid @NotNull @RequestBody UserDto user) {
        log.info("Post user");
        return userClient.save(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@Valid @NotNull @PathVariable Long userId) {
        log.info("GET user by id {}", userId);
        return userClient.getById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Valid @NotNull @RequestBody UserUpdateDto userDto,
                                         @Valid @NotNull @PathVariable Long userId) {
        log.info("PATCH user {}", userId);
        return userClient.update(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@Valid @NotNull @PathVariable Long userId) {
        log.info("DELETE user {}", userId);
        return userClient.delete(userId);
    }
}
