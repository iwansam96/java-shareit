package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.util.List;

public interface BookingService {

    BookingDto add(BookingDtoInput bookingDtoInput, Long userId);

    BookingDto setApproveById(Long bookingId, Boolean isApproved, Long userId);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getByUserId(Long userId, BookingState state, Integer from, Integer size);

    List<BookingDto> getByItemsByUserId(Long userId, String state, Integer from, Integer size);
}
