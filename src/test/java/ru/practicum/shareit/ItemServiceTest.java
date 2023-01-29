package ru.practicum.shareit;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.IMarkerFactory;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exception.ItemDataIsIncorrectException;
import ru.practicum.shareit.exception.ItemEditingByNonOwnerException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.constraints.AssertTrue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class ItemServiceTest {
    ItemRepository itemRepository;
    UserRepository userRepository;
    BookingRepository bookingRepository;
    CommentRepository commentRepository;
    ItemRequestRepository itemRequestRepository;
    ItemService itemService;

    @BeforeEach
    public void init() {
        itemRepository = Mockito.mock(ItemRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        commentRepository = Mockito.mock(CommentRepository.class);
        itemRepository = Mockito.mock(ItemRepository.class);
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository,
                itemRequestRepository);
    }

    //    add userId incorrect
    @Test
    public void shouldThrowUserNotFoundExceptionWhenAddItemWithIncorrectUserId() {
        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        Assertions.assertThrows(UserNotFoundException.class, () -> itemService.add(99L, itemDto));
    }

    //    add itemDto = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhenItemDtoIsNull() {
        var user = new User();
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, null));
    }

    //    add new item
    @Test
    public void shouldReturnNewItemWhenAdd() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        var actual = itemService.add(1L, itemDto);
        Assertions.assertEquals(itemDto, actual);
    }

    //    add new item name = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullName() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }

    //    add new item description = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullDescription() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }

    //    add new item available = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenAddWhithNullAvailable() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setName("itemDto");
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDto));

        Assertions.assertThrows(ItemDataIsIncorrectException.class, () -> itemService.add(1L, itemDto));
    }


    //    edit item
    @Test
    public void shouldReturnEditedItem() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        var actual = itemService.edit(1L, 1L, itemDtoEdited);
        Assertions.assertEquals(itemDtoEdited, actual);
    }

    //    edit userId incorrect
    @Test
    public void shouldThrowItemEditingByNonOwnerExceptionWhenEditNotOwner() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemEditingByNonOwnerException.class,
                () -> itemService.edit(2L, 1L, itemDtoEdited));
    }

//    edit itemId incorrect
    @Test
    public void shouldThrowItemNotFoundExceptionWhenEditWithItemIdIsIncorrect() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemNotFoundException.class,
                () -> itemService.edit(1L, 99L, itemDtoEdited));
    }

//    edit itemDto = null
    @Test
    public void shouldThrowItemDataIsIncorrectExceptionWhenEditWithItemDtoIsNull() {
        var user = new User();
        user.setId(1L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemDataIsIncorrectException.class,
                () -> itemService.edit(1L, 99L, null));
    }

//    edit item by not owner
    @Test
    public void shouldThrowItemEditingByNonOwnerExceptionWhenEditByNotOwner() {
        var user = new User();
        user.setId(1L);
        var user2 = new User();
        user2.setId(2L);

        var itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("itemDto");
        itemDto.setAvailable(true);
        itemDto.setDescription("itemDto description");
        itemDto.setOwner(user);
        itemDto.setBookings(new HashSet<>());
        itemDto.setComments(new ArrayList<>());

        var itemDtoEdited = new ItemDto();
        itemDtoEdited.setName("itemDto");
        itemDtoEdited.setAvailable(true);
        itemDtoEdited.setDescription("edited");
        itemDtoEdited.setOwner(user);
        itemDtoEdited.setBookings(new HashSet<>());
        itemDtoEdited.setComments(new ArrayList<>());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(ItemMapper.toItem(itemDto)));
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(ItemMapper.toItem(itemDtoEdited));

        Assertions.assertThrows(ItemEditingByNonOwnerException.class,
                () -> itemService.edit(2L, 1L, itemDtoEdited));
    }

//    get by id
//    get by id itemId is incorrect
//    get by id userId is incorrect
//    get by id item not found
//    get by id bookings not found
//    get by id comments not found

//    get by ownerId
//    get by ownerId userId is incorrect
//    get by ownerId item not found
//    get by ownerId bookings not found
//    get by ownerId comments not found

//    search
//    search text = null
//    search text is empty
//    search bookings not found
//    search comments not found

//    get by item request
//    get by item request item not found
//    get by item request bookings not found
//    get by item request comments not found
}
