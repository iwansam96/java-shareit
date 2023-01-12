package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;

@Data
public class ItemMapper {
    public static ItemDto toItemDto(Item item, Set<Booking> bookings, Long userId, List<CommentDto> comments) {
        if (item == null)
            return null;
        ItemDto itemDto =  new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getRequest() != null ? item.getRequest().getId() : null);
        itemDto.setBookings(bookings);
        itemDto.setComments(comments);

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
        if (itemDto == null)
            return null;
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}
