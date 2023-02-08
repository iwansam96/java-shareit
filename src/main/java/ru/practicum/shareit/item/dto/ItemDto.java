package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class ItemDto {
    @Positive
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private User owner;

    @Positive
    private Long requestId;

    private Set<Booking> bookings;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;
}
