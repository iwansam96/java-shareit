package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.exception.*;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingDto add(BookingDtoInput bookingDtoInput, Long userId) throws ItemIsNotAvailableException, ItemNotFoundException, BookingDatesAreIncorrectException, UserNotFoundException, BookingItemByOwnerException;

    BookingDto setApproveById(Long bookingId, Boolean isApproved, Long userId) throws BookingApprovePermissionsException, BookingApproveAfterApproveException;

    BookingDto getById(Long bookingId, Long userId) throws BookingNotFoundException;

    List<BookingDto> getByUserId(Long userId, Optional<String> state) throws UserNotFoundException, StateIsIncorrectException;

    List<BookingDto> getByItemsByUserId(Long userId, Optional<String> state) throws UserNotFoundException, StateIsIncorrectException;
}
