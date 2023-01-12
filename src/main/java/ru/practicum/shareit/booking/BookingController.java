package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@Valid @RequestBody BookingDtoInput bookingDtoInput,
                          @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("POST /bookings");

        try {
            return bookingService.add(bookingDtoInput, userId);
        } catch (ItemIsNotAvailableException | BookingDatesAreIncorrectException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (ItemNotFoundException | BookingItemByOwnerException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setApproveById(@PathVariable Long bookingId,
                                     @RequestParam(name = "approved") Boolean isApproved,
                                     @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("PATCH /bookings/{}?{}", bookingId, isApproved);

        try {
            return bookingService.setApproveById(bookingId, isApproved, userId);
        } catch (BookingApprovePermissionsException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (BookingApproveAfterApproveException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId,
                              @Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /bookings/{}", bookingId);

        try {
            return bookingService.getById(bookingId, userId);
        } catch (BookingNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<BookingDto> getByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam Optional<String> state) {
        log.info("GET /bookings?state={}", state);

        try {
            return bookingService.getByUserId(userId, state);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (StateIsIncorrectException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/owner")
    public List<BookingDto> getByItemsByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam Optional<String> state) {
        log.info("GET /bookings/owner?state={}", state);

        try {
            return bookingService.getByItemsByUserId(userId, state);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (StateIsIncorrectException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler
    private ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
