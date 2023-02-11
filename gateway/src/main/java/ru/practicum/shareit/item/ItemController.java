package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentClient;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private final CommentClient commentClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @NotNull @RequestBody ItemDto itemDto) {
        log.info("Post item userId={}", userId);
        return itemClient.add(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> edit(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                        @Valid @NotNull @RequestBody ItemUpdateDto itemDto,
                        @Valid @NotNull @PathVariable Long itemId) {
        log.info("Patch item {}, userId={}", itemId, userId);
        return itemClient.edit(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable @Valid @NotNull Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get items bi owner {}, from={}, size={}", userId, from, size);

        return itemClient.getByOwnerId(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        System.out.println(text);
        log.info("Get items by search text={}, from={}, size={}", text, from, size);

        return itemClient.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @PathVariable @NotNull Long itemId,
                                 @Valid @RequestBody @NotNull CommentDtoInput commentDtoInput) {
        log.info("Post comment, itemId={}, userId={}", itemId, userId);

        return commentClient.addComment(userId, itemId, commentDtoInput);
    }
}
