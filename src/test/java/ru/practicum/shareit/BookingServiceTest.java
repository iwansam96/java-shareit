package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class BookingServiceTest {
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingService bookingService;

    @BeforeEach
    public void init() {
        bookingRepository = Mockito.mock(BookingRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }

    //    add
    @Test
    public void shouldReturnBookingWhenAdd() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        var actual = bookingService.add(bookingDtoInput, bookerId);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual);
    }

    //    add booking dtoInput null
    @Test
    public void shouldThrowBookingInputDataIsIncorrectExceptionWhenAddWithDtoInputIsNull() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingInputDataIsIncorrectException.class,
                () -> bookingService.add(null, bookerId));
    }

    //    add userId null
    @Test
    public void shouldThrowUserNotFoundExceptionWhenAddWithUserIdIsIncorrect() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.add(bookingDtoInput, null));
    }

    //    add item not found
    @Test
    public void shouldThrowItemNotFoundExceptionWhenAddWithItemIdIsIncorect() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = null;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> bookingService.add(bookingDtoInput, bookerId));
    }

    //    add item is not available
    @Test
    public void shouldThrowItemIsNotAvailableExceptionWhenAddWithItemIsNotAvailable() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(false);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(ItemIsNotAvailableException.class,
                () -> bookingService.add(bookingDtoInput, bookerId));
    }

    //    add booker is owner
    @Test
    public void shouldThrowBookingItemByOwnerExceptionWhenAddWithOwnerAsBooker() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingItemByOwnerException.class,
                () -> bookingService.add(bookingDtoInput, userId));
    }

    //    add end is before start
    @Test
    public void shouldThrowBookingDatesAreIncorrectExceptionWhenAddWithEndIsBeforeStart() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(2));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(1));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingDatesAreIncorrectException.class,
                () -> bookingService.add(bookingDtoInput, bookerId));
    }

    //    add end is before now
    @Test
    public void shouldThrowBookingDatesAreIncorrectExceptionWhenAddWithEndIsBeforeNow() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().minusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingDatesAreIncorrectException.class,
                () -> bookingService.add(bookingDtoInput, bookerId));
    }

    //    add start is before now
    @Test
    public void shouldThrowBookingDatesAreIncorrectExceptionWhenAddWithStartIsBeforeNow() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().minusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingDatesAreIncorrectException.class,
                () -> bookingService.add(bookingDtoInput, bookerId));
    }


    //    set approve
    @Test
    public void shouldReturnApprovedBookingWhenSetApprove() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 3L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        var actual = bookingService.setApproveById(bookingId, true, userId);
        booking.setStatus(BookingStatus.APPROVED);
        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual);
    }

    //    set approve booking id is incorrect
    @Test
    public void shouldReturnNullWhenSetApproveWithBookingIdIsIncorrect() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 3L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertNull(bookingService.setApproveById(null, true, userId));
    }

    //    set approve isApproved is null
    @Test
    public void shouldReturnRejectedBookingWhenSetApproveIsApprovedIsNull() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 3L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        var actual = bookingService.setApproveById(bookingId, null, userId);
        booking.setStatus(BookingStatus.REJECTED);
        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual);
    }

    //    set approve by not owner
    @Test
    public void shouldThrowBookingApprovePermissionsExceptionWhenSetApproveByNotOwner() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 3L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingApprovePermissionsException.class,
                () -> bookingService.setApproveById(bookingId, true, bookerId));
    }

    //    set approve after approve
    @Test
    public void shouldThrowBookingApproveAfterApproveExceptionWhenSetApproveAfterApprove() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 3L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setStatus(BookingStatus.APPROVED);

        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(booker));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(booking);

        Assertions.assertThrows(BookingApproveAfterApproveException.class,
                () -> bookingService.setApproveById(bookingId, true, userId));
    }

    //    get by id
    @Test
    public void shouldReturnBookingWhenGetById() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(Optional.of(booking));

        var actual = bookingService.getById(bookingId, userId);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual);
    }

    //    get by id booking not found
    @Test
    public void shouldThrowBookingNotFoundExceptionWhenGetByIdWithIncorectId() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.getById(99L, userId));
    }

    //    get by id user is not owner/booker
    @Test
    public void shouldThrowBookingNotFoundExceptionWhenGetByIdWithUserIsNotOwnerOrBooker() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(bookingRepository.findById(Mockito.any())).thenReturn(Optional.of(booking));

        Assertions.assertThrows(BookingNotFoundException.class, () -> bookingService.getById(bookingId, 99L));
    }


    //    get by user id
    @Test
    public void shouldReturnBookingWhenGetByUserId() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getByUserIdAndStatus(Mockito.any(PageRequest.class), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByUserId(userId, Optional.of("ALL"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }

    //    get by user id user not found
    @Test
    public void shouldThrowUserNotFoundExceptionWhenGetByUserIdWithIncorrectUserId() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(bookingRepository.getByUserIdAndStatus(Mockito.any(PageRequest.class), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getByUserId(null, Optional.of("ALL"), from, size));
    }

    //    get by user id booking state is current
    @Test
    public void shouldReturnBookingWithCurrentStateWhenGetByUserIdAndCurrentState() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.APPROVED);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getCurrentByUserIdAndStatus(Mockito.any(PageRequest.class), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByUserId(userId, Optional.of("CURRENT"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }

    //    get by user id booking state is past
    @Test
    public void shouldReturnBookingWithPastStateWhenGetByUserIdAndPastState() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.CANCELLED);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getPastByUserIdAndStatus(Mockito.any(PageRequest.class), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByUserId(userId, Optional.of("PAST"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }


    //    get by items by user id (get users items -> get bookings for this items)
    @Test
    public void shouldReturnBookingWhenGetByItemsByUserId() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getByOwnerIdAndStatus(Mockito.any(), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByItemsByUserId(userId, Optional.of("ALL"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }

    //    get by items by user id user id is incorrect
    @Test
    public void shouldThrow() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(null)).thenReturn(Optional.empty());
        Mockito.when(bookingRepository.getByOwnerIdAndStatus(Mockito.any(), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        Assertions.assertThrows(UserNotFoundException.class,
                () -> bookingService.getByItemsByUserId(null, Optional.of("ALL"), from, size));
    }

    //    get by items by user id stateString is null
    @Test
    public void shouldThrowStatusIsUnsupportedExceptionWhenGetByItemByUserIdWithStateIsNull() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getByOwnerIdAndStatus(Mockito.any(), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByItemsByUserId(userId, null, from, size);
        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }

//    get by items by user id State is CURRENT
    @Test
    public void shoudReturnBookingWithCurrentStateWhenGetByItemByUserIdAndStateIsCurrent() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getCurrentByOwnerIdAndStatus(Mockito.any(), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByItemsByUserId(userId, Optional.of("CURRENT"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }

//    get by items by user id State is PAST
    @Test
    public void shoudReturnBookingWithPastStateWhenGetByItemByUserIdAndStateIsPast() {
        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        Long userId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        Long bookingId = 5L;
        int from = 0;
        int page = 1;
        int size = 10;

        User user = new User();
        user.setId(userId);

        User booker = new User();
        booker.setId(bookerId);

        Item item = new Item();
        item.setId(itemId);
        item.setName("item 1");
        item.setDescription("description item 1");
        item.setAvailable(true);
        item.setOwner(user);

        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));

        Booking booking = BookingMapper.toBooking(bookingDtoInput, item, user, BookingStatus.WAITING);
        booking.setId(bookingId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bookingRepository.getPastByOwnerIdAndStatus(Mockito.any(), Mockito.eq(userId),
                Mockito.any())).thenReturn(List.of(booking));

        var actual = bookingService.getByItemsByUserId(userId, Optional.of("PAST"), from, size);

        Assertions.assertEquals(BookingMapper.toBookingDto(booking), actual.get(0));
    }
}
