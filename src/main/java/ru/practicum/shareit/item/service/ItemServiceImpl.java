package ru.practicum.shareit.item.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.comment.dto.CommentMapper;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    @NonNull
    private ItemRepository itemRepository;

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private BookingRepository bookingRepository;

    @NonNull
    private CommentRepository commentRepository;

    @NonNull
    private ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        if (itemDto == null)
            throw new ItemDataIsIncorrectException("new item cannot be null");
        boolean isAvailableNull = itemDto.getAvailable() == null;
        boolean isNameIncorrect = itemDto.getName() == null || itemDto.getName().isBlank();
        boolean isDescriptionIncorrect = itemDto.getDescription() == null || itemDto.getDescription().isBlank();
        if (isAvailableNull || isNameIncorrect || isDescriptionIncorrect) {
            throw new ItemDataIsIncorrectException("available, name or description cannot be empty");
        }

        User owner = userRepository.findById(userId).orElse(null);
        Item itemToCreate = ItemMapper.toItem(itemDto);
        if (owner == null)
            throw new UserNotFoundException("user with id " + userId + " not found");
        itemToCreate.setOwner(owner);

        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(itemDto.getRequestId()).orElse(null);
            itemToCreate.setRequest(itemRequest);
        }

        Item item = itemRepository.save(itemToCreate);
        return ItemMapper.toItemDto(item, bookingRepository.getBookingsByItem_Id(item.getId()), userId,
                commentRepository.getCommentsByItem_Id(item.getId()).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public ItemDto edit(Long userId, Long itemId, ItemDto itemDto) {
        if (itemDto == null)
            throw new ItemDataIsIncorrectException("itemDto is null");
        Item oldItem = itemRepository.findById(itemId).orElse(null);
        if (oldItem == null) {
            throw new ItemNotFoundException("item " + itemId + " not found");
        }

        if (!Objects.equals(oldItem.getOwner().getId(), userId))
            throw new ItemEditingByNonOwnerException("user " + userId + " cannot edit item " + itemId);
        Item newItem = ItemMapper.toItem(itemDto);
        if (newItem.getName() != null)
            oldItem.setName(newItem.getName());
        if (newItem.getDescription() != null)
            oldItem.setDescription(newItem.getDescription());
        if (newItem.getAvailable() != null)
            oldItem.setAvailable(newItem.getAvailable());
        Item updatedItem = itemRepository.save(oldItem);
        return ItemMapper.toItemDto(updatedItem,
                bookingRepository.getBookingsByItem_Id(updatedItem.getId()), userId,
                commentRepository.getCommentsByItem_Id(itemId).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public ItemDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if (item == null)
            throw new ItemNotFoundException("item with id " + itemId + " not found");

        return ItemMapper.toItemDto(item, bookingRepository.getBookingsByItem_Id(itemId), userId,
                commentRepository.getCommentsByItem_Id(itemId).stream()
                        .map(CommentMapper::toCommentDto)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<ItemDto> getByOwnerId(Long userId, Integer from, Integer size) {
        if (from == null || from < 0 || size == null || size < 0)
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        int page = from / size;

        return itemRepository.findByOwnerId(PageRequest.of(page, size), userId).stream()
                .map(nextItem -> ItemMapper.toItemDto(nextItem,
                        bookingRepository.getBookingsByItem_Id(nextItem.getId()),
                        userId, commentRepository.getCommentsByItem_Id(nextItem.getId()).stream()
                                .map(CommentMapper::toCommentDto)
                                .collect(Collectors.toList())))
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }

        if (from == null || from < 0 || size == null || size < 0)
            throw new PaginationParametersAreIncorrectException("'from' or 'size' is null or < 0");
        int page = from / size;

        return itemRepository.search(PageRequest.of(page, size), text.toLowerCase()).stream()
                .map(nextItem -> ItemMapper.toItemDto(nextItem,
                        bookingRepository.getBookingsByItem_Id(nextItem.getId()),
                        null, commentRepository.getCommentsByItem_Id(nextItem.getId()).stream()
                                .map(CommentMapper::toCommentDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getByItemRequest(ItemRequest itemRequest) {
        return itemRepository.getItemsByRequest(itemRequest).stream()
                .map(nextItem -> ItemMapper.toItemDto(nextItem,
                        bookingRepository.getBookingsByItem_Id(nextItem.getId()),
                        null, commentRepository.getCommentsByItem_Id(nextItem.getId()).stream()
                                .map(CommentMapper::toCommentDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }
}
