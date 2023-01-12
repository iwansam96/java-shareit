package ru.practicum.shareit.item.service;

import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.ItemDataIsIncorrectException;

import java.util.List;

public interface ItemService {
    ItemDto add(Long userId, ItemDto itemDto) throws ItemDataIsIncorrectException, UserNotFoundException;

    ItemDto edit(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long itemId, Long userId);

    List<ItemDto> getByOwnerId(Long userId);

    List<ItemDto> search(String text);
}
