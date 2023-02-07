package ru.practicum.shareit.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.ItemRequestDescriptionIsInvalidException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemRequestsServiceUnitTest {
    ItemRequestRepository itemRequestRepository;
    UserService userService;
    ItemService itemService;
    ItemRequestService itemRequestService;

    @BeforeEach
    public void init() {
        itemRequestRepository = Mockito.mock(ItemRequestRepository.class);
        userService = Mockito.mock(UserService.class);
        itemService = Mockito.mock(ItemService.class);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userService, itemService);
    }

    //    add
    @Test
	@DisplayName("Test add method")
    public void shouldReturnItemRequest() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        var actual = itemRequestService.add(requestorId, itemRequestDtoInput);
        Assertions.assertEquals(itemRequest, ItemRequestMapper.toItemRequest(requestor, actual));
//        Assertions.assertEquals(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()), actual);
    }

    //    add itemRequestDtoInput is null
    @Test
	@DisplayName("Test add method throws ItemRequestDescriptionIsInvalidException (request is null)")
    public void shouldThrowItemRequestDescriptionIsInvalidExceptionWhenAddWithItemRequestDtoInputIsNull() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        Assertions.assertThrows(ItemRequestDescriptionIsInvalidException.class,
                () -> itemRequestService.add(requestorId, null));
    }

    //    add text in itemRequestDtoInput is null
    @Test
	@DisplayName("Test add method throws ItemRequestDescriptionIsInvalidException (request text is null)")
    public void shouldThrowItemRequestDescriptionIsInvalidExceptionWhenAddWithTextInItemRequestDtoInputIsNull() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = null;
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        Assertions.assertThrows(ItemRequestDescriptionIsInvalidException.class,
                () -> itemRequestService.add(requestorId, itemRequestDtoInput));
    }

    //    add text in itemRequestDtoInput is empty
    @Test
	@DisplayName("Test add method throws ItemRequestDescriptionIsInvalidException (request text is empty)")
    public void shouldThrowItemRequestDescriptionIsInvalidExceptionWhenAddWithTextInItemRequestDtoInputIsEmpty() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);

        Assertions.assertThrows(ItemRequestDescriptionIsInvalidException.class,
                () -> itemRequestService.add(requestorId, itemRequestDtoInput));
    }

    //    getByRequestorId
    @Test
	@DisplayName("Test getByRequestorId method")
    public void shouldReturnItemRequestListWhenGetByRequestorId() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);

        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()));

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.getItemRequestsByRequestor(requestor)).thenReturn(itemRequestList);

        var actual = itemRequestService.getByRequestorId(requestorId);
        Assertions.assertEquals(itemRequestDtoList, actual);
    }

    //    getAllByPages
    @Test
	@DisplayName("Test getAllByPages method")
    public void shouldReturnItemRequestListWhenGetAllByPages() {
        int page = 1;
        int size = 10;
        int from = 0;
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);
        List<ItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);

        List<ItemRequestDto> itemRequestDtoList = new ArrayList<>();
        itemRequestDtoList.add(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()));

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.getItemRequestsByRequestorIsNot(Mockito.any(PageRequest.class),
                Mockito.eq(requestor))).thenReturn(itemRequestList);

        var actual = itemRequestService.getAllByPages(requestorId, from, size);
        Assertions.assertEquals(itemRequestDtoList, actual);
    }

    //    getByRequestId
    @Test
	@DisplayName("Test getByRequestId method")
    public void shouldReturnItemRequestWhenGetByRequestId() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        var actual = itemRequestService.getByRequestId(requestorId, requestId);
        Assertions.assertEquals(ItemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>()), actual);
    }

    //    getByRequestId requestId is incorrect
    @Test
	@DisplayName("Test getByRequestId method throws ItemRequestNotFoundException")
    public void shouldThrowItemRequestNotFoundExceptionWhenGetByRequestIdWithRequestIdIsIncorrect() {
        Long requestorId = 1L;
        User requestor = new User();
        requestor.setId(requestorId);


        Long requestId = 3L;
        String description = "item request description";
        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(description);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now().minusHours(1));
        itemRequest.setId(requestId);
        itemRequest.setDescription(description);

        Long ownerId = 4L;
        User owner = new User();
        owner.setId(ownerId);

        Long itemId = 2L;
        Item item = new Item();
        item.setId(itemId);
        item.setRequest(itemRequest);
        item.setName("item name");
        item.setOwner(owner);

        Mockito.when(userService.getById(requestorId)).thenReturn(UserMapper.toUserDto(requestor));
        Mockito.when(itemRequestRepository.findById(requestId)).thenReturn(Optional.of(itemRequest));

        Assertions.assertThrows(ItemRequestNotFoundException.class,
                () -> itemRequestService.getByRequestId(requestorId, 99L));
    }
}
