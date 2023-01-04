package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    private Long id;

    @NotEmpty
    @NotBlank
    @NotNull
    private String name;

    @NotEmpty
    @NotBlank
    @NotNull
    private String description;

    @NotNull
    private Boolean available;

    private User owner;

    private ItemRequest request;
}
