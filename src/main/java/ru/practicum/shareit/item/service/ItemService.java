package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemService {
    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto edit(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long itemId, Long userId);

    List<ItemDto> getByOwnerId(Long userId, Integer from, Integer size);

    List<ItemDto> search(String text, Integer from, Integer size);

    List<ItemDto> getByItemRequest(ItemRequest itemRequest);
}
