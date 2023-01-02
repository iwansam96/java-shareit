package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryItemRepository implements ItemRepository {
    private List<Item> items = new ArrayList<>();

    @Override
    public Item add(Item item) {
        item.setId(IdGenerator.generateId("item"));
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
                .filter(nextItem -> nextItem.getId() == itemId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Item> getByOwnerId(Long userId) {
        List<Item> itemsByOwnerId = new ArrayList<>();
        for (Item item : items) {
            if (item.getOwner().getId() == userId)
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
                System.out.println(item);
                searchResult.add(item);
            }
        }
        return searchResult;
    }
}
