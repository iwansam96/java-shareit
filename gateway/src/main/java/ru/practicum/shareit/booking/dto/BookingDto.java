package ru.practicum.shareit.booking.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@Validated
public class BookingDto {
    @Positive
    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    @Positive
    private Long bookerId;

    private BookingStatus status;
}
