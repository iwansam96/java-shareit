package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(
            @Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @Valid @NotNull @RequestBody ItemRequestDtoInput itemRequestDtoInput
    ) {
        log.info("POST /requests");
        return requestClient.addRequest(requestorId, itemRequestDtoInput);
    }

    @GetMapping
    public ResponseEntity<Object> getByRequestorId(
            @Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long requestorId
    ) {
        log.info("GET /requests");
        return requestClient.getByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllByPages(
            @Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Valid @Positive @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("GET /requests/all?from={}&size={}", from, size);
        return requestClient.getAllByPages(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getByRequestId(
            @Valid @NotNull @Positive @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @Positive @PathVariable Long requestId
    ) {
        log.info("GET /requests/{}", requestId);
        return requestClient.getByRequestId(userId, requestId);
    }
}
