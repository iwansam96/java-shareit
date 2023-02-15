package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@RequiredArgsConstructor
@Validated
public class UserDto {
    @Positive
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
