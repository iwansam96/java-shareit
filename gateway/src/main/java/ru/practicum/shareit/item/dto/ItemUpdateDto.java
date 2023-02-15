package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
}
