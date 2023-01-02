package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Item add(Long userId, Item item) {
        User owner = userRepository.getById(userId);
        if (owner != null) {
            item.setOwner(owner);
            itemRepository.add(item);
            return item;
        }
        return null;
    }

    @Override
    public Item edit(Long userId, Long itemId, ItemDto itemDto) {
        Item oldItem = itemRepository.getById(itemId);
        if (oldItem != null) {
            if (oldItem.getOwner().getId() != userId)
                return null;
            Item newItem = ItemMapper.toItem(itemDto);
            if (newItem.getName() != null)
                oldItem.setName(newItem.getName());
            if (newItem.getDescription() != null)
                oldItem.setDescription(newItem.getDescription());
            if (newItem.getAvailable() != null)
                oldItem.setAvailable(newItem.isAvailable());
            itemRepository.update(oldItem);
            return oldItem;
        } else
            return null;
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.getById(itemId);
    }

    @Override
    public List<Item> getByOwnerId(Long userId) {
        return itemRepository.getByOwnerId(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemRepository.search(text);
    }
}
