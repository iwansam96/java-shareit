package ru.practicum.shareit.exception;

public class ItemRequestDescriptionIsInvalidException extends RuntimeException {
    public ItemRequestDescriptionIsInvalidException(String message) {
        super(message);
    }
}
