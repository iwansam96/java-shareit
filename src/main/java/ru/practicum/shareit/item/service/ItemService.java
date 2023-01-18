package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto edit(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long itemId, Long userId);

    List<ItemDto> getByOwnerId(Long userId);

    List<ItemDto> search(String text);
}
