package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ItemServiceUnitTest {
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;
    ItemService itemService;

    @BeforeEach
    public void init() {
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository,
                itemRequestRepository);
    }

    //    add userId incorrect
    @Test
    public void shouldThrowUserNotFoundExceptionWhenAddItemWithIncorrectUserId() {
        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.add(99L, itemDto));
    }

    //    add itemDto = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhenItemDtoIsNull() {
        var user = new User();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, null));
    }

    //    add new item
    @Test
    public void shouldReturnNewItemWhenAdd() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        var actual = itemService.add(1L, itemDto);
        Assertions.assertEquals(itemDto, actual);
    }

    //    add new item name = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullName() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }

    //    add new item description = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullDescription() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }

    //    add new item available = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullAvailable() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }


    //    edit item
    @Test
    public void shouldReturnEditedItem() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        var actual = itemService.edit(1L, 1L, itemDtoEdited);
        Assertions.assertEquals(itemDtoEdited, actual);
    }

    //    edit userId incorrect
    @Test
    public void shouldThrowItemEditingByNonOwnerExceptionWhenEditNotOwner() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemEditingByNonOwnerException.class,
                () -> itemService.edit(2L, 1L, itemDtoEdited));
    }

//    edit itemId incorrect
    @Test
    public void shouldThrowItemNotFoundExceptionWhenEditWithItemIdIsIncorrect() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.edit(1L, 99L, itemDtoEdited));
    }

//    edit itemDto = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenEditWithItemDtoIsNull() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemDataIsIncorrectException.class,
                () -> itemService.edit(1L, 99L, null));
    }

//    edit item by not owner
    @Test
    public void shouldThrowItemEditingByNonOwnerExceptionWhenEditByNotOwner() {
        var user = new User();
        user.setId(1L);
        var user2 = new User();
        user2.setId(2L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemEditingByNonOwnerException.class,
                () -> itemService.edit(2L, 1L, itemDtoEdited));
    }


//    get by id
    @Test
    public void shouldReturnItemWhenGetById() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.getById(1L, 1L);
        Assertions.assertEquals(commentsDto, actual.getComments());
        Assertions.assertEquals(bookings, actual.getBookings());
        Assertions.assertEquals(itemDto, actual);
    }

//    get by id itemId is incorrect
    @Test
    public void shouldThrowItemNotFoundExceptionWhenGetByIdWithIncorrectItemid() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        Assertions.assertThrows(ItemNotFoundException.class, () -> itemService.getById(99L, 1L));
    }

//    get by id userId is not owner
    @Test
    public void shouldReturnItemWithoutNextAndLastBookingsWhenGetByIdWithNotOwnerId() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);

        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.getById(1L, 99L);
        Assertions.assertEquals(commentsDto, actual.getComments());
        Assertions.assertEquals(bookings, actual.getBookings());
        Assertions.assertEquals(itemDto, actual);
    }

//    get by ownerId
    @Test
    public void shouldReturnListWithItemWhenGetByOwnerId() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        int size = 10;
        int from = 0;

        Mockito.when(itemRepository.findItemsByOwner_Id(Mockito.any(), Mockito.eq(1L))).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.getByOwnerId(1L, from, size);

        Assertions.assertEquals(commentsDto, actual.get(0).getComments());
        Assertions.assertEquals(bookings, actual.get(0).getBookings());
        Assertions.assertEquals(itemDto, actual.get(0));
    }

//    get by ownerId userId is incorrect
    @Test
    public void shouldReturnEmptyListWhenGetByOwnerIdWithIncorrectId() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        int size = 10;
        int from = 0;

        Mockito.when(itemRepository.findItemsByOwner_Id(Mockito.any(), Mockito.eq(1L))).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.getByOwnerId(99L, from, size);

        Assertions.assertEquals(new ArrayList<>(), actual);
    }

    @Test
    public void shouldThrowPaginationParametersAreIncorrectExceptionWhenGetByOwnerIdWithIncorrectSize() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        int size = 10;
        int from = 0;

        Mockito.when(itemRepository.findItemsByOwner_Id(Mockito.any(), Mockito.eq(1L))).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        Assertions.assertThrows(PaginationParametersAreIncorrectException.class,
                () -> itemService.getByOwnerId(1L, from, -99));
    }


//    search
    @Test
    public void shouldReturnItemWithWordInDescriptionWhenSearch() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description with special word");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        int size = 10;
        int from = 0;

        String text = "word";

        Mockito.when(itemRepository.findItemsByDescriptionContainingIgnoreCaseAndAvailableIsTrue(Mockito.any(), Mockito.eq(text))).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.search(text, from, size);

        Assertions.assertEquals(commentsDto, actual.get(0).getComments());
        Assertions.assertEquals(bookings, actual.get(0).getBookings());
        Assertions.assertEquals(itemDto, actual.get(0));
    }

//    search text = null
    @Test
    public void shouldReturnEmptyListWhenSearchWhenTextIsNull() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        int size = 10;
        int from = 0;

        String text = null;

        Mockito.when(itemRepository.findItemsByDescriptionContainingIgnoreCaseAndAvailableIsTrue(Mockito.any(), Mockito.eq(text))).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.search(text, from, size);

        Assertions.assertEquals(new ArrayList<>(), actual);
    }

//    search text is empty
@Test
public void shouldReturnEmptyListWhenSearchWhenTextIsEmpty() {
    var user = new User();
    user.setId(1L);

    List<Comment> comments = new ArrayList<>();
    Comment comment = new Comment();
    comment.setId(2L);
    comment.setText("one beautiful comment");
    comment.setCreated(LocalDateTime.now());
    comment.setAuthor(user);
    comments.add(comment);
    var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

    Set<Booking> bookings = new HashSet<>();
    Booking booking = new Booking();
    booking.setStart(LocalDateTime.now().minusDays(2));
    booking.setEnd(LocalDateTime.now().minusDays(1));
    booking.setBooker(user);
    bookings.add(booking);

    var itemDto = new ItemDto();
    itemDto.setId(1L);
    itemDto.setName("itemDto");
    itemDto.setAvailable(true);
    itemDto.setDescription("itemDto description");
    itemDto.setOwner(user);
    itemDto.setBookings(bookings);
    itemDto.setComments(commentsDto);
    itemDto.setLastBooking(BookingMapper.toBookingDto(booking));

    var itemDtoList = new ArrayList<Item>();
    itemDtoList.add(ItemMapper.toItem(itemDto));

    int size = 10;
    int from = 0;

    String text = "";

    Mockito.when(itemRepository.findItemsByDescriptionContainingIgnoreCaseAndAvailableIsTrue(Mockito.any(), Mockito.eq(text))).thenReturn(itemDtoList);
    Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
    Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

    var actual = itemService.search(text, from, size);

    Assertions.assertEquals(new ArrayList<>(), actual);
}


//    get by item request
    @Test
    public void shouldReturnItemWhenGetByItemRequest() {
        var user = new User();
        user.setId(1L);

        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("one beautiful comment");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comments.add(comment);
        var commentsDto = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());

        Set<Booking> bookings = new HashSet<>();
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setBooker(user);
        bookings.add(booking);

        var request = new ItemRequest();
        request.setDescription("request description");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now().minusDays(5));
        request.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(bookings);
        itemDto.setComments(commentsDto);
        itemDto.setLastBooking(BookingMapper.toBookingDto(booking));
        itemDto.setRequestId(1L);

        var itemDtoList = new ArrayList<Item>();
        itemDtoList.add(ItemMapper.toItem(itemDto));

        Mockito.when(itemRepository.getItemsByRequest(request)).thenReturn(itemDtoList);
        Mockito.when(bookingRepository.getBookingsByItem_Id(1L)).thenReturn(bookings);
        Mockito.when(commentRepository.getCommentsByItem_Id(1L)).thenReturn(comments);

        var actual = itemService.getByItemRequest(request);

        Assertions.assertEquals(commentsDto, actual.get(0).getComments());
        Assertions.assertEquals(bookings, actual.get(0).getBookings());
        Assertions.assertEquals(ItemMapper.toItem(itemDto), ItemMapper.toItem(actual.get(0)));
    }
}
