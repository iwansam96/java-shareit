package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.exception.CommentBeforeBookingException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemTextForSearchIsEmptyException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class CommentServiceUnitTest {
    CommentRepository commentRepository;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    CommentService commentService;

    @BeforeEach
    public void init() {
        commentRepository = Mockito.mock(CommentRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentService = new CommentServiceImpl(commentRepository, userRepository, itemRepository, bookingRepository);
    }

    //    add comment
    @Test
    @DisplayName("Test addComment method")
    public void shouldReturnNewComment() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment text");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        var actual = commentService.addComment(user2.getId(), itemId, commentDtoInput);

        Assertions.assertEquals(comment, CommentMapper.toComment(actual, user2, item));
    }

    //    add comment userId is incorrect
    @Test
    @DisplayName("Test addComment method throws UserNotFoundException")
    public void shouldThrowUserNotFoundExceptionWhenAddCommentWithIncorrectUserId() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment text");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> commentService.addComment(99L, itemId, commentDtoInput));
    }

    //    add comment itemId is incorrect
    @Test
    @DisplayName("Test addComment method throws ItemNotFoundException")
    public void shouldThrowItemNotFoundExceptionWhenAddCommentWithIncorrectItemId() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment text");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> commentService.addComment(user2.getId(), 99L, commentDtoInput));
    }

    //    add comment commentDtoInput is null
    @Test
    @DisplayName("Test addComment method throws ItemTextForSearchIsEmptyException (comment is null)")
    public void shouldThrowItemTextForSearchIsEmptyExceptionWhenAddCommentWithItemDtoInputIsNull() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment text");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ItemTextForSearchIsEmptyException.class,
                () -> commentService.addComment(user2.getId(), itemId, null));
    }

    //    add comment text in commentDtoInput is null
    @Test
    @DisplayName("Test addComment method throws ItemTextForSearchIsEmptyException (comment text is null)")
    public void shouldThrowItemTextForSearchIsEmptyExceptionWhenAddCommentWithCommentTextIsNull() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText(null);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ItemTextForSearchIsEmptyException.class,
                () -> commentService.addComment(user2.getId(), itemId, commentDtoInput));
    }

    //    add comment commentDtoInput is blank
    @Test
    @DisplayName("Test addComment method throws ItemTextForSearchIsEmptyException (comment text is blank)")
    public void shouldThrowItemTextForSearchIsEmptyExceptionWhenTextInCommentDtoInputIsBlank() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user2);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(2));
        booking.setItem(item);
        booking.setStatus(BookingStatus.CANCELLED);
        var bookingList = new ArrayList<Booking>();
        bookingList.add(booking);

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(bookingList);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(ItemTextForSearchIsEmptyException.class,
                () -> commentService.addComment(user2.getId(), itemId, commentDtoInput));
    }

    //    add comment before booking
    @Test
    @DisplayName("Test addComment method throws CommentBeforeBookingException")
    public void shouldThrowCommentBeforeBookingExceptionWhenAddCommentBeforeBooking() {
        Long userId = 1L;
        Long itemId = 3L;
        Long bookingId = 4L;
        Long commentId = 5L;

        User user = new User();
        user.setId(userId);
        user.setName("user name");
        user.setEmail("user@email");

        User user2 = new User();
        user2.setId(userId + 1);
        user2.setName("user2 name");
        user2.setEmail("user2@email");

        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName("item name");
        item.setDescription("item description");

        Comment comment = new Comment();
        comment.setAuthor(user2);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment.setText("comment text");
        comment.setId(commentId);

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment text");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(userId + 1)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.getBookingsByBooker_IdAndEndBefore(Mockito.any(), Mockito.any()))
                .thenReturn(new ArrayList<>());
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        Assertions.assertThrows(CommentBeforeBookingException.class,
                () -> commentService.addComment(user2.getId(), itemId, commentDtoInput));
    }
}
