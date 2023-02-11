package ru.practicum.shareit.exception;

public class ItemEditingByNonOwnerException extends RuntimeException {
    public ItemEditingByNonOwnerException(String message) {
        super(message);
    }
}
