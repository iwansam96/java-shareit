package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto add(Long requestorId, ItemRequestDtoInput itemRequestDtoInput);

    List<ItemRequestDto> getByRequestorId(Long requestorId);

    List<ItemRequestDto> getAllByPages(Long userId, Integer from, Integer size);

    ItemRequestDto getByRequestId(Long userId, Long requestId);
}
