package ru.practicum.shareit.booking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
    @NonNull
    private BookingService bookingService;

    @PostMapping
    public BookingDto add(@Valid @RequestBody BookingDtoInput bookingDtoInput,
                          @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /bookings");

        return bookingService.add(bookingDtoInput, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApproveById(@PathVariable Long bookingId,
                                     @RequestParam(name = "approved") Boolean isApproved,
                                     @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH /bookings/{}?{}", bookingId, isApproved);

        return bookingService.setApproveById(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId,
                              @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /bookings/{}", bookingId);

        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam Optional<String> state,
                                        @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                        @Valid @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("GET /bookings?state={}", state);

        return bookingService.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByItemsByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam Optional<String> state,
                                               @Valid @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Valid @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("GET /bookings/owner?state={}", state);

        return bookingService.getByItemsByUserId(userId, state, from, size);
    }
}
