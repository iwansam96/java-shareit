package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class BookingServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    @DisplayName("Test add method")
    public void add() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto booker = userService.save(UserMapper.toUserDto(user2));

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(item1Saved.getId());
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());

        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        Booking bookingFromDB = query.getSingleResult();

        Assertions.assertEquals(bookingSaved, BookingMapper.toBookingDto(bookingFromDB));
    }

    @Test
    @DisplayName("Test setApproveById method")
    public void setApproveById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto booker = userService.save(UserMapper.toUserDto(user2));

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(item1Saved.getId());
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());
        bookingSaved.setStatus(BookingStatus.APPROVED);

        bookingService.setApproveById(bookingSaved.getId(), true, owner.getId());

        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        Booking bookingFromDB = query.getSingleResult();

        Assertions.assertEquals(bookingSaved, BookingMapper.toBookingDto(bookingFromDB));
    }

    @Test
    @DisplayName("Test getById method")
    public void getById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto booker = userService.save(UserMapper.toUserDto(user2));

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(item1Saved.getId());
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());

        var bookingFromGet = bookingService.getById(bookingSaved.getId(), booker.getId());

        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        Booking bookingFromDB = query.getSingleResult();

        Assertions.assertEquals(bookingFromGet, BookingMapper.toBookingDto(bookingFromDB));
    }

    @Test
    @DisplayName("Test getByUserId method")
    public void getByUserId() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto booker = userService.save(UserMapper.toUserDto(user2));

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(item1Saved.getId());
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());

        var bookingFromGet = bookingService.getByUserId(booker.getId(), Optional.of("ALL"), 0, 10);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        List<Booking> bookingFromDB = query.getResultList();

        Assertions.assertEquals(bookingFromGet, bookingFromDB.stream().map(BookingMapper::toBookingDto).collect(
                Collectors.toList()));
    }

    @Test
    @DisplayName("Test getByItemsByUserId method")
    public void getByItemsByUserId() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto booker = userService.save(UserMapper.toUserDto(user2));

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        BookingDtoInput bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(item1Saved.getId());
        bookingDtoInput.setStart(LocalDateTime.now().plusDays(1));
        bookingDtoInput.setEnd(LocalDateTime.now().plusDays(2));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());

        var bookingFromGet = bookingService.getByItemsByUserId(owner.getId(), Optional.of("ALL"), 0, 10);

        TypedQuery<Booking> query = em.createQuery("select b from Booking b", Booking.class);
        List<Booking> bookingFromDB = query.getResultList();

        Assertions.assertEquals(bookingFromGet, bookingFromDB.stream().map(BookingMapper::toBookingDto).collect(
                Collectors.toList()));
    }
}
