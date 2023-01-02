package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);
    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public Item add(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody Item item) {
        log.info("POST /items");
        if (userId == null || item == null) {
            log.error("userId or item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Item newItem = itemService.add(userId, item);
        if (newItem != null)
            return newItem;
        else {
            log.error("item is not added");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{itemId}")
    public Item edit(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                     @RequestBody ItemDto itemDto,
                     @PathVariable Long itemId) {
        log.info("PATCH /items");
        if (userId == null || itemId == null) {
            log.error("userId or item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Item editedItem = itemService.edit(userId, itemId, itemDto);
        if (editedItem != null)
            return editedItem;
        else {
            log.error("you are not owner or item not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.info("GET /items/{}", itemId);
        if (itemId == null) {
            log.error("item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Item item = itemService.getById(itemId);
        if (item == null) {
            log.error("item not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return item;
    }

    @GetMapping
    public List<Item> getByOwnerId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /items");
        if (userId == null) {
            log.error("userId is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Item> usersItems = itemService.getByOwnerId(userId);
        if (usersItems == null) {
            log.error("items not found or error in ItemRepository");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return usersItems;
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        log.info("GET /items/search?text={}", text);
        if (text == null) {
            log.error("search string is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Item> foundedItems = itemService.search(text.toLowerCase());
        if (foundedItems == null) {
            log.error("items not found or error in ItemRepository");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return foundedItems;
    }
}
