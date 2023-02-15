package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(User requestor, ItemRequestDtoInput itemRequestDtoInput) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDtoInput.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }

    public static ItemRequest toItemRequest(User requestor, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setRequestor(requestor);
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setId(itemRequestDto.getId());

        return itemRequest;
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemDto> items) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setItems(items);

        return itemRequestDto;
    }
}
