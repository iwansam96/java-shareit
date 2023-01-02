package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
public class User {
    private Long id;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;
}
