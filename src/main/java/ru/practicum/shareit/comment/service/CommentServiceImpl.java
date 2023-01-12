package ru.practicum.shareit.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.exception.CommentBeforeBookingException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private BookingRepository bookingRepository;

    @Autowired
    CommentServiceImpl(CommentRepository commentRepository,
                       UserRepository userRepository,
                       ItemRepository itemRepository,
                       BookingRepository bookingRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
    }

    public CommentDto addComment(Long userId, Long itemId, CommentDtoInput commentDtoInput) throws UserNotFoundException, ItemNotFoundException, CommentBeforeBookingException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new UserNotFoundException("user with id " + userId + " not found");

        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null)
            throw new ItemNotFoundException("item with id " + itemId + " not found");

        List<Booking> bookings = bookingRepository.getBookingsByBooker_IdAndEndBefore(userId, LocalDateTime.now());
        if (bookings.size() < 1)
            throw new CommentBeforeBookingException("user " + userId + " can't comment before booking");

        Comment comment = new Comment();
        comment.setText(commentDtoInput.getText());
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        Comment newComment = commentRepository.save(comment);
        return CommentMapper.toCommentDto(newComment);
    }

}
