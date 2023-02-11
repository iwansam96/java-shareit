package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

@Data
@RequiredArgsConstructor
public class UserUpdateDto {
    @Positive
    private Long id;

    private String name;

    @Email
    private String email;
}
