package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(@Valid @NotNull User requestor,
                                            @Valid @NotNull ItemRequestDtoInput itemRequestDtoInput) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDtoInput.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(@Valid @NotNull ItemRequest itemRequest,
                                                  @Valid @NotNull List<ItemDto> items) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(items);

        return itemRequestDto;
    }
}
