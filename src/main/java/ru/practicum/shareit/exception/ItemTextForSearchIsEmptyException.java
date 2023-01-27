package ru.practicum.shareit.exception;

public class ItemTextForSearchIsEmptyException extends RuntimeException {
    public ItemTextForSearchIsEmptyException(String message) {
        super(message);
    }
}
