package ru.practicum.shareit.exception;

public class BookingDatesAreIncorrectException extends RuntimeException {
    public BookingDatesAreIncorrectException(String string) {
        super(string);
    }
}
