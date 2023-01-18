package ru.practicum.shareit.exception;

public class CommentBeforeBookingException extends RuntimeException {
    public CommentBeforeBookingException(String message) {
        super(message);
    }
}
