package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                              @RequestBody ItemRequestDtoInput itemRequestDtoInput) {
        log.info("POST /requests");

        return itemRequestService.add(requestorId, itemRequestDtoInput);
    }

    @GetMapping
    public List<ItemRequestDto> getByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("GET /requests");
        return itemRequestService.getByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllByPages(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam Integer from, @RequestParam Integer size) {

        log.info("GET /requests/all?from={}&size={}", from, size);

        return itemRequestService.getAllByPages(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {

        log.info("GET /requests/{}", requestId);

        return itemRequestService.getByRequestId(userId, requestId);
    }

}
