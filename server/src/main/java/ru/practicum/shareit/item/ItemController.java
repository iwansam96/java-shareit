package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;

    private final CommentService commentService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("POST /items");
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto,
                        @PathVariable Long itemId) {
        log.info("PATCH /items");
        return itemService.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("GET /items/{}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam Integer from,
                                      @RequestParam Integer size) {
        log.info("GET /items");
        return itemService.getByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text, @RequestParam Integer from, @RequestParam Integer size) {
        log.info("GET /items/search?text={}", text);
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                 @RequestBody CommentDtoInput commentDtoInput) {
        log.info("POST /items/{}/comment", itemId);
        return commentService.addComment(userId, itemId, commentDtoInput);
    }
}
