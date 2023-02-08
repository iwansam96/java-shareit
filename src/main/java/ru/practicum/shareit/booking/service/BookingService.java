package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingDto add(BookingDtoInput bookingDtoInput, Long userId);

    BookingDto setApproveById(Long bookingId, Boolean isApproved, Long userId);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getByUserId(Long userId, Optional<String> state, Integer from, Integer size);

    List<BookingDto> getByItemsByUserId(Long userId, Optional<String> state, Integer from, Integer size);
}
