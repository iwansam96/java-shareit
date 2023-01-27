package ru.practicum.shareit.exception;

public class StatusIsUnsupportedException extends RuntimeException {
    public StatusIsUnsupportedException(String message) {
        super(message);
    }
}
