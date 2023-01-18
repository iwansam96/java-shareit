package ru.practicum.shareit.exception;

public class BookingApproveAfterApproveException extends RuntimeException {
    public BookingApproveAfterApproveException(String message) {
        super(message);
    }
}
