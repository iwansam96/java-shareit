package ru.practicum.shareit.exception;

public class BookingInputDataIsIncorrectException extends RuntimeException {
    public BookingInputDataIsIncorrectException(String message) {
        super(message);
    }
}
