package ru.practicum.shareit.exception;

public class UserWithSameEmailException extends Exception{
    public UserWithSameEmailException(String message) {
        super(message);
    }
}
