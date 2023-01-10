package ru.practicum.shareit.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exception.IncorrectItemDataException;
import ru.practicum.shareit.item.service.ItemService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
    public ItemDto add(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("POST /items");
        if (userId == null || itemDto == null) {
            log.error("userId or item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            return itemService.add(userId, itemDto);
        } catch (IncorrectItemDataException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException | EntityNotFoundException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                     @RequestBody ItemDto itemDto,
                     @PathVariable Long itemId) {
        log.info("PATCH /items");
        if (userId == null || itemId == null) {
            log.error("userId or item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ItemDto editedItem = itemService.edit(userId, itemId, itemDto);
        if (editedItem == null) {
            log.error("you are not owner or item not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return editedItem;
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("GET /items/{}", itemId);
        if (itemId == null) {
            log.error("item is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ItemDto item = itemService.getById(itemId);
        if (item == null) {
            log.error("item not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return item;
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /items");
        if (userId == null) {
            log.error("userId is null");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<ItemDto> usersItems = itemService.getByOwnerId(userId);
        if (usersItems == null) {
            log.error("items not found or error in ItemRepository");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return usersItems;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET /items/search?text={}", text);
        if (text == null || text.isBlank()) {
            log.error("search string is null");
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return new ArrayList<>();
        }
        List<ItemDto> foundedItems = itemService.search(text.toLowerCase());
        if (foundedItems == null) {
            log.error("items not found or error in ItemRepository");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return foundedItems;
    }
}
