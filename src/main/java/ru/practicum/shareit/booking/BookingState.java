package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.StatusIsUnsupportedException;

import java.util.Optional;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState stringToState(Optional<String> stateString) {
        try {
            BookingState state = BookingState.ALL;
            if (stateString.isPresent()) {
                state = BookingState.valueOf(stateString.get());
            }
            return state;
        } catch (IllegalArgumentException e) {
            throw new StatusIsUnsupportedException("{\"error\": \"Unknown state: UNSUPPORTED_STATUS\"}");
        }
    }
}
