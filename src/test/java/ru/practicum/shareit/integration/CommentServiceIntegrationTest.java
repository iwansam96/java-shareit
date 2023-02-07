package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.comment.service.CommentService;
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

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class CommentServiceIntegrationTest {
    private final EntityManager em;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final CommentService commentService;

    @Test
    @DisplayName("Test addComment method")
    public void addComment() {
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
        bookingDtoInput.setStart(LocalDateTime.now().plusSeconds(3));
        bookingDtoInput.setEnd(LocalDateTime.now().plusSeconds(5));
        BookingDto bookingSaved = bookingService.add(bookingDtoInput, booker.getId());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText("comment for item1");

        var commentFromAdd = commentService.addComment(booker.getId(), item1Saved.getId(), commentDtoInput);

        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        Comment commentFromDB = query.getSingleResult();

        Assertions.assertEquals(commentFromAdd, CommentMapper.toCommentDto(commentFromDB));
    }
}
