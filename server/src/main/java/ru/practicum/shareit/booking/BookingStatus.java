package ru.practicum.shareit.booking;

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
