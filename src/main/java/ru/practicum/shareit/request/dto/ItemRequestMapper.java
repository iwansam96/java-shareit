package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

    public static ItemRequest toItemRequest(@Valid @NotNull User requestor,
                                            @Valid @NotNull ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setId(itemRequestDto.getId());

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
