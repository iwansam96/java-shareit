package ru.practicum.shareit.comment.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class CommentDtoInput {
    @NotBlank
    private String text;
}
