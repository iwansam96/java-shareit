package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;

public class ItemMapper {
    public static ItemDto toItemDto(Item item, Set<Booking> bookings, Long userId, List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
        itemDto.setBookings(bookings);
        itemDto.setComments(comments);
        if (item.getOwner() != null)
            itemDto.setOwner(item.getOwner());
        if (item.getRequest() != null)
            itemDto.setRequestId(item.getRequest().getId());

        if (Objects.equals(item.getOwner().getId(), userId)) {
            Optional<Booking> last = bookings.stream()
                    .filter(nextBooking -> nextBooking.getEnd().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(Booking::getEnd));
            itemDto.setLastBooking(BookingMapper.toBookingDto(last.orElse(null)));

            Optional<Booking> next = bookings.stream()
                    .filter(nextBooking -> nextBooking.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(Booking::getStart));
            itemDto.setNextBooking(BookingMapper.toBookingDto(next.orElse(null)));
        }

        return itemDto;
    }

    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        if (itemDto.getId() != null)
            item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        if (itemDto.getOwner() != null)
            item.setOwner(itemDto.getOwner());
        return item;
    }
}
