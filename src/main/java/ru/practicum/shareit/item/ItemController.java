package ru.practicum.shareit.item;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @NonNull
    private ItemService itemService;

    @NonNull
    private CommentService commentService;

    @PostMapping
    public ItemDto add(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        log.info("POST /items");
        return itemService.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto edit(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                        @Valid @NotNull @RequestBody ItemDto itemDto,
                        @Valid @NotNull @PathVariable Long itemId) {
        log.info("PATCH /items");
        return itemService.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable @Valid @NotNull Long itemId) {
        log.info("GET /items/{}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET /items");
        return itemService.getByOwnerId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.info("GET /items/search?text={}", text);
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody CommentDtoInput commentDtoInput) {
        log.info("POST /items/{}/comment", itemId);

        return commentService.addComment(userId, itemId, commentDtoInput);
    }
}
