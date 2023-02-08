package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDto {

    @Positive
    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    @NotNull
    private List<ItemDto> items;
}
