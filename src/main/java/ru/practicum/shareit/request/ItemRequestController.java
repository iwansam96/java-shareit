package ru.practicum.shareit.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    @NonNull
    private ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto add(@NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
                              @NotNull @RequestBody ItemRequestDtoInput itemRequestDtoInput) {
        log.info("POST /requests");

        return itemRequestService.add(requestorId, itemRequestDtoInput);
    }

    @GetMapping
    public List<ItemRequestDto> getByRequestorId(
            @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("GET /requests");
        return itemRequestService.getByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllByPages(
            @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("GET /requests/all?from={}&size={}", from, size);

        return itemRequestService.getAllByPages(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getByRequestId(@NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @Positive @PathVariable Long requestId) {

        log.info("GET /requests/{}", requestId);

        return itemRequestService.getByRequestId(userId, requestId);
    }

}
