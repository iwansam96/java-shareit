package ru.practicum.shareit.exception;

public class PaginationParametersAreIncorrectException extends RuntimeException {
    public PaginationParametersAreIncorrectException(String message) {
        super(message);
    }
}
