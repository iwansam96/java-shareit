package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;

public interface CommentService {
    CommentDto addComment(Long userId, Long itemId, CommentDtoInput commentDtoInput);
}
