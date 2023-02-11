package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.StateIsIncorrectException;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getByUserId(
            @Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") long userId,
            @Valid @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @Valid @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Valid @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new StateIsIncorrectException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getByUserId(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                           @Valid @NotNull @RequestBody BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @NotNull @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> setApproveById(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @Valid @NotNull @PathVariable Long bookingId,
                                                 @Valid @NotNull @RequestParam(name = "approved") Boolean isApproved) {
        log.info("Patch booking {}, userId={}, approved={}", bookingId, userId, isApproved);

        return bookingClient.setApproveById(bookingId, isApproved, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByItemsByUserId(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam(name = "state", defaultValue = "all") String stateParam,
                             @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                             @Valid @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get bookings /owner?state={}, userId={}, from={}, size={}", stateParam, userId, from, size);

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new StateIsIncorrectException("Unknown state: " + stateParam));

        return bookingClient.getByItemsByUserId(userId, state, from, size);
    }
}
