package ru.practicum.shareit.request.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestDescriptionIsInvalidException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.PaginationParametersAreIncorrectException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    @NonNull
    private ItemRequestRepository itemRequestRepository;

    @NonNull
    private UserService userService;

    @NonNull
    private ItemService itemService;

    @Override
    public ItemRequestDto add(Long requestorId, ItemRequestDtoInput itemRequestDtoInput) {
        if (itemRequestDtoInput.getDescription() == null || itemRequestDtoInput.getDescription().isBlank())
            throw new ItemRequestDescriptionIsInvalidException("item request description is null or blank");

        User requestor = UserMapper.toUser(userService.getById(requestorId));

        ItemRequest newItemRequest = itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(requestor, itemRequestDtoInput)
        );

        return ItemRequestMapper.toItemRequestDto(newItemRequest, new ArrayList<>());
    }

    @Override
    public List<ItemRequestDto> getByRequestorId(Long requestorId) {
        User requestor = UserMapper.toUser(userService.getById(requestorId));
        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequestsByRequestor(requestor);

        return itemRequests.stream().map(
                        nextItemRequest -> ItemRequestMapper.toItemRequestDto(nextItemRequest,
                                itemService.getByItemRequest(nextItemRequest)))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllByPages(Long userId, Integer from, Integer size) {
        if (from == null || from < 0 || size == null || size < 0)
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        int page = from / size;

        User user = UserMapper.toUser(userService.getById(userId));
        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequestsByRequestorIsNot(
                PageRequest.of(page, size), user);

        return itemRequests.stream().map(
                        nextItemRequest -> ItemRequestMapper.toItemRequestDto(nextItemRequest,
                                itemService.getByItemRequest(nextItemRequest)))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getByRequestId(Long userId, Long requestId) {
//        throws UserNotFoundException if user does not exist
        UserMapper.toUser(userService.getById(userId));

        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElse(null);
        if (itemRequest == null)
            throw new ItemRequestNotFoundException("item request " + requestId + " not found");

        return ItemRequestMapper.toItemRequestDto(itemRequest,
                itemService.getByItemRequest(itemRequest));
    }
}
