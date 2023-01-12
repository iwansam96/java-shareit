package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.exception.CommentBeforeBookingException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;

public interface CommentService {
    CommentDto addComment(Long userId, Long itemId, CommentDtoInput commentDtoInput) throws UserNotFoundException, ItemNotFoundException, CommentBeforeBookingException;
}
