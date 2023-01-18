package ru.practicum.shareit.exception;

public class UserDataIsIncorrectException extends RuntimeException {
    public UserDataIsIncorrectException(String message) {
        super(message);
    }
}
