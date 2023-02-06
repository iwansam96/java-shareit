package ru.practicum.shareit.booking.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    @NonNull
    private BookingRepository bookingRepository;

    @NonNull
    private ItemRepository itemRepository;

    @NonNull
    private UserRepository userRepository;


    @Override
    public BookingDto add(BookingDtoInput bookingDtoInput, Long userId) {
        if (bookingDtoInput == null)
            throw new BookingInputDataIsIncorrectException("booking input data is incorrect");
        Item item = itemRepository.findById(bookingDtoInput.getItemId()).orElse(null);
        if (item == null) {
            throw new ItemNotFoundException("item with id " + bookingDtoInput.getItemId() + " not found");
        } else if (!item.getAvailable()) {
            throw new ItemIsNotAvailableException("item with id " + item.getId() + " is not available");
        } else if (Objects.equals(item.getOwner().getId(), userId)) {
            throw new BookingItemByOwnerException("user " + userId + " is owner of item " + item.getOwner().getId());
        }

        LocalDateTime start = bookingDtoInput.getStart();
        LocalDateTime end = bookingDtoInput.getEnd();
        if (end.isBefore(start) || start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            throw new BookingDatesAreIncorrectException("dates start= " + start + " or end= " + end + "are incorrect");
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " is not found");

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto setApproveById(Long bookingId, Boolean isApproved, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null)
            return null;

        if (!Objects.equals(userId, booking.getItem().getOwner().getId())) {
            String message = "user " + userId + " can't approve booking " + bookingId +
                    " booker: " + booking.getBooker().getId();
            throw new BookingApprovePermissionsException(message);
        }

        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new BookingApproveAfterApproveException("booking " + bookingId + " is already approved");
        }

        if (isApproved != null && isApproved)
            booking.setStatus(BookingStatus.APPROVED);
        else
            booking.setStatus(BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new BookingNotFoundException("booking with id " + bookingId + " not found");
        }
        if (!(Objects.equals(userId, booking.getBooker().getId())) && !(Objects.equals(userId,
                booking.getItem().getOwner().getId())))
            throw new BookingNotFoundException("booking of user " + userId + " or with item of this user not found");
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getByUserId(Long userId, Optional<String> stateString, Integer from, Integer size) {
        if (from == null || from < 0 || size == null || size < 0)
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        int page = from / size;

        BookingState state = BookingState.stringToState(stateString);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " not found");

        List<Booking> bookings;
        if (state == BookingState.CURRENT)
            bookings = bookingRepository.getCurrentByUserIdAndStatus(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));
        else if (state == BookingState.PAST)
            bookings = bookingRepository.getPastByUserIdAndStatus(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));
        else
            bookings = bookingRepository.findBookingsByBooker_IdAndStatusInOrderByStartDesc(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getByItemsByUserId(Long userId, Optional<String> stateString, Integer from, Integer size) {
        if (from == null || from < 0 || size == null || size < 0)
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " not found");

        if (stateString == null || stateString.isEmpty()) {
            stateString = Optional.of("ALL");
        }
        BookingState state = BookingState.stringToState(stateString);

        if (from == null || from < 0 || size == null || size < 0) {
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        }
        int page = from / size;

        List<Booking> bookings;
        if (state == BookingState.CURRENT)
            bookings = bookingRepository.getCurrentByOwnerIdAndStatus(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));
        else if (state == BookingState.PAST)
            bookings = bookingRepository.getPastByOwnerIdAndStatus(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));
        else
            bookings = bookingRepository.findBookingsByItem_Owner_IdAndStatusInOrderByStartDesc(PageRequest.of(page, size), userId,
                    BookingStatus.stateToStatuses(state));

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}
