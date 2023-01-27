package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.StateIsIncorrectException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED;

    public static List<BookingStatus> stateToStatuses(BookingState state) {
        if (!Arrays.asList(BookingState.values()).contains(state))
            throw new StateIsIncorrectException("state " + state + " is incorrect");

        List<BookingStatus> statuses = new ArrayList<>();
        if (state.equals(BookingState.REJECTED))
            statuses.add(BookingStatus.REJECTED);
        else if (state.equals(BookingState.WAITING))
            statuses.add(BookingStatus.WAITING);
        else
            statuses = Arrays.stream(BookingStatus.values()).collect(Collectors.toList());

        return statuses;
    }
}
