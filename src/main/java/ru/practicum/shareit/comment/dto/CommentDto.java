package ru.practicum.shareit.comment.dto;

import lombok.Data;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    @Positive
    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
