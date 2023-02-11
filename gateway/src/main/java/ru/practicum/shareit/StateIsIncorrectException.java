package ru.practicum.shareit;

public class StateIsIncorrectException extends RuntimeException {
    public StateIsIncorrectException(String message) {
        super(message);
    }
}
