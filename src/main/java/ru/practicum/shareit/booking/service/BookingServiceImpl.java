package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BookingDto add(BookingDtoInput bookingDtoInput, Long userId) throws ItemIsNotAvailableException, ItemNotFoundException, BookingDatesAreIncorrectException, UserNotFoundException, BookingItemByOwnerException {
        Booking booking = new Booking();

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

        booking.setItem(item);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);

        Booking newBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(newBooking);
    }

    @Override
    public BookingDto setApproveById(Long bookingId, Boolean isApproved, Long userId) throws BookingApprovePermissionsException, BookingApproveAfterApproveException {
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

        if (isApproved)
            booking.setStatus(BookingStatus.APPROVED);
        else
            booking.setStatus(BookingStatus.REJECTED);

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) throws BookingNotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new BookingNotFoundException("booking with id " + bookingId + " not found");
        }
        if (!(Objects.equals(userId, booking.getBooker().getId())) && !(Objects.equals(userId, booking.getItem().getOwner().getId())))
            throw new BookingNotFoundException("booking of user " + userId + " or with item of this user not found");
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getByUserId(Long userId, Optional<String> stateString) throws UserNotFoundException,
            StateIsIncorrectException, IllegalArgumentException {
        BookingState state = stringToState(stateString);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " not found");

        List<Booking> bookings;
        if (state == BookingState.CURRENT)
            bookings = bookingRepository.getCurrentByUserIdAndStatus(userId, stateToStatuses(state));
        else if (state == BookingState.PAST)
            bookings = bookingRepository.getPastByUserIdAndStatus(userId, stateToStatuses(state));
        else
            bookings = bookingRepository.getByUserIdAndStatus(userId, stateToStatuses(state));

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getByItemsByUserId(Long userId, Optional<String> stateString) throws UserNotFoundException,
            StateIsIncorrectException, IllegalArgumentException {
        BookingState state = stringToState(stateString);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " not found");

        List<Booking> bookings;
        if (state == BookingState.CURRENT)
            bookings = bookingRepository.getCurrentByOwnerIdAndStatus(userId, stateToStatuses(state));
        else if (state == BookingState.PAST)
            bookings = bookingRepository.getPastByOwnerIdAndStatus(userId, stateToStatuses(state));
        else
            bookings = bookingRepository.getByOwnerIdAndStatus(userId, stateToStatuses(state));

        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    private BookingState stringToState(Optional<String> stateString) throws IllegalArgumentException {
        try {
            BookingState state = BookingState.ALL;
            if (stateString.isPresent()) {
                state = BookingState.valueOf(stateString.get());
            }
            return state;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("{\"error\": \"Unknown state: UNSUPPORTED_STATUS\"}");
        }
    }

    private List<BookingStatus> stateToStatuses(BookingState state) throws StateIsIncorrectException {
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
