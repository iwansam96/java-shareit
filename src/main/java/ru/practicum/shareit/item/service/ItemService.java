package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item add(Long userId, Item item);
    Item edit(Long userId, Long itemId, ItemDto itemDto);
    Item getById(Long itemId);
    List<Item> getByOwnerId(Long userId);
    List<Item> search(String text);
}
