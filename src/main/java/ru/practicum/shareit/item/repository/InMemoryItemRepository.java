package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.IdGenerator;
import ru.practicum.shareit.util.IdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private List<Item> items = new ArrayList<>();

    @Override
    public Item add(Item item) {
        item.setId(IdGenerator.generateId(IdType.ITEM));
        items.add(item);
        return item;
    }

    @Override
    public Item update(Item item) {
        Item itemToUpdate = getById(item.getId());
        if (itemToUpdate != null)
            items.remove(itemToUpdate);
        items.add(item);
        return item;
    }

    @Override
    public Item getById(Long itemId) {
        return items.stream()
                .filter(nextItem -> Objects.equals(nextItem.getId(), itemId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Item> getByOwnerId(Long userId) {
        List<Item> itemsByOwnerId = new ArrayList<>();
        for (Item item : items) {
            if (Objects.equals(item.getOwner().getId(), userId))
                itemsByOwnerId.add(item);
        }
        return itemsByOwnerId;
    }

    @Override
    public List<Item> search(String text) {
        List<Item> searchResult = new ArrayList<>();
        if (text.isBlank())
            return searchResult;
        for (Item item : items) {
            if (item.getDescription().toLowerCase().contains(text) && item.getAvailable()) {
                searchResult.add(item);
            }
        }
        return searchResult;
    }
}
