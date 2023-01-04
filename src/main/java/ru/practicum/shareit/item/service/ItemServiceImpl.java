package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.exception.IncorrectItemDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) throws IncorrectItemDataException, UserNotFoundException {
        boolean isAvailableNull = itemDto.getAvailable() == null;
        boolean isNameIncorrect = itemDto.getName() == null || itemDto.getName().isBlank();
        boolean isDescriptionIncorrect = itemDto.getDescription() == null || itemDto.getDescription().isBlank();
        if (isAvailableNull || isNameIncorrect || isDescriptionIncorrect) {
            throw new IncorrectItemDataException("available, name or description cannot be empty");
        }
        User owner = userRepository.getById(userId);
        Item item = ItemMapper . toItem(itemDto);
        if (owner == null)
            throw new UserNotFoundException("user with id " + userId + " not found");
        item.setOwner(owner);
        itemRepository.add(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto edit(Long userId, Long itemId, ItemDto itemDto) {
        Item oldItem = itemRepository.getById(itemId);
        if (oldItem != null) {
            if (!Objects.equals(oldItem.getOwner().getId(), userId))
                return null;
            Item newItem = ItemMapper.toItem(itemDto);
            if (newItem.getName() != null)
                oldItem.setName(newItem.getName());
            if (newItem.getDescription() != null)
                oldItem.setDescription(newItem.getDescription());
            if (newItem.getAvailable() != null)
                oldItem.setAvailable(newItem.getAvailable());
            itemRepository.update(oldItem);
            return ItemMapper.toItemDto(oldItem);
        } else
            return null;
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.getById(itemId);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getByOwnerId(Long userId) {
        return itemRepository.getByOwnerId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
