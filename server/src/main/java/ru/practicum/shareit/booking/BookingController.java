package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto bookItem(@RequestBody BookingDtoInput bookingDtoInput,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /bookings");

        return bookingService.add(bookingDtoInput, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApproveById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId,
                                     @RequestParam(name = "approved") Boolean isApproved) {
        log.info("PATCH /bookings/{}?{}", bookingId, isApproved);

        return bookingService.setApproveById(bookingId, isApproved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("GET /bookings/{}", bookingId);

        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam BookingState state,
                                        @RequestParam Integer from,
                                        @RequestParam Integer size) {
        log.info("GET /bookings?state={}", state);

        return bookingService.getByUserId(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam String state,
                                               @RequestParam Integer from,
                                               @RequestParam Integer size) {
        log.info("GET /bookings/owner?state={}", state);

        return bookingService.getByItemsByUserId(userId, state, from, size);
    }
}
