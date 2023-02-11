package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@RequiredArgsConstructor
@Validated
public class ItemUpdateDto {
    private String name;
    private String description;
    private Boolean available;
}
