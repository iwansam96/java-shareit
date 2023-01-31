package ru.practicum.shareit.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemServiceIntegrationTest {
    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    @Test
    public void add() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto user1Saved = userService.save(UserMapper.toUserDto(user1));

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        ItemDto item1Saved = itemService.add(user1Saved.getId(), ItemMapper.toItemDto(item1, new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));

        TypedQuery<Item> query = em.createQuery("select i from Item i", Item.class);
        List<Item> itemsFromDB = query.getResultList();

        Assertions.assertEquals(item1Saved, ItemMapper.toItemDto(itemsFromDB.get(0), new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));
    }

    @Test
    public void edit() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto user1Saved = userService.save(UserMapper.toUserDto(user1));

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        ItemDto item1Saved = itemService.add(user1Saved.getId(), ItemMapper.toItemDto(item1, new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));

        item1.setDescription("item1 updated description");
        ItemDto item1Updated = itemService.edit(user1Saved.getId(), item1Saved.getId(),
                ItemMapper.toItemDto(item1, new HashSet<>(),
                        user1Saved.getId(), new ArrayList<>()));

        TypedQuery<Item> query = em.createQuery("select i from Item i", Item.class);
        List<Item> itemsFromDB = query.getResultList();

        Assertions.assertEquals(item1Updated, ItemMapper.toItemDto(itemsFromDB.get(0), new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));
    }

    @Test
    public void getById() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto user1Saved = userService.save(UserMapper.toUserDto(user1));

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        ItemDto item1Saved = itemService.add(user1Saved.getId(), ItemMapper.toItemDto(item1, new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));

        ItemDto item1FromGet = itemService.getById(item1Saved.getId(), user1Saved.getId());

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.id = :itemId", Item.class);
        Item item1FromDB = query.setParameter("itemId", item1Saved.getId()).getSingleResult();

        Assertions.assertEquals(item1FromGet,
                ItemMapper.toItemDto(item1FromDB, new HashSet<>(), user1Saved.getId(), new ArrayList<>()));
    }

    @Test
    public void getByOwnerId() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto user1Saved = userService.save(UserMapper.toUserDto(user1));

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        ItemDto item1Saved = itemService.add(user1Saved.getId(), ItemMapper.toItemDto(item1, new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));

        List<ItemDto> item1FromGet = itemService.getByOwnerId(user1Saved.getId(), 0, 10);

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.owner.id = :ownerId", Item.class);
        Item item1FromDB = query.setParameter("ownerId", user1Saved.getId()).getSingleResult();
        List<ItemDto> item1fromDBList = new ArrayList<>();
        item1fromDBList.add(ItemMapper.toItemDto(item1FromDB, new HashSet<>(), user1Saved.getId(), new ArrayList<>()));

        Assertions.assertEquals(item1FromGet, item1fromDBList);
    }

    @Test
    public void search() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto user1Saved = userService.save(UserMapper.toUserDto(user1));

        Item item1 = new Item();
        item1.setName("item1");
        item1.setDescription("item1 very interesting description");
        item1.setAvailable(true);
        item1.setOwner(user1);
        ItemDto item1Saved = itemService.add(user1Saved.getId(), ItemMapper.toItemDto(item1, new HashSet<>(),
                user1Saved.getId(), new ArrayList<>()));

        String searchText = "interesting";

        List<ItemDto> item1FromGet = itemService.search(searchText, 0, 10);

        TypedQuery<Item> query = em.createQuery(
                "select i from Item i where lower(i.description) like lower(concat('%', :text, '%'))",
                Item.class
        );
        Item item1FromDB = query.setParameter("text", searchText).getSingleResult();
        List<ItemDto> item1fromDBList = new ArrayList<>();
        item1fromDBList.add(ItemMapper.toItemDto(item1FromDB, new HashSet<>(), user1Saved.getId(), new ArrayList<>()));

        Assertions.assertEquals(item1FromGet, item1fromDBList);
    }

    @Test
    public void getByItemRequest() {
        var user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@email.com");
        UserDto owner = userService.save(UserMapper.toUserDto(user1));

        var user2 = new User();
        user2.setName("user2");
        user2.setEmail("user2@email.com");
        UserDto requestor = userService.save(UserMapper.toUserDto(user2));

        ItemRequestDtoInput itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription("item request description");
        var itemRequestDto = itemRequestService.add(requestor.getId(), itemRequestDtoInput);

        ItemDto item1Dto = new ItemDto();
        item1Dto.setName("item1dto");
        item1Dto.setDescription("item1dto description");
        item1Dto.setAvailable(true);
        item1Dto.setOwner(UserMapper.toUser(owner));
        item1Dto.setRequestId(itemRequestDto.getId());
        ItemDto item1Saved = itemService.add(owner.getId(), item1Dto);

        List<ItemDto> item1FromGet = itemService.getByItemRequest(
                ItemRequestMapper.toItemRequest(UserMapper.toUser(requestor), itemRequestDto));

        TypedQuery<Item> query = em.createQuery("select i from Item i where i.request = :request", Item.class);
        Item item1FromDB = query.setParameter("request",
                ItemRequestMapper.toItemRequest(UserMapper.toUser(requestor), itemRequestDto)).getSingleResult();
        List<ItemDto> item1fromDBList = new ArrayList<>();
        item1fromDBList.add(
                ItemMapper.toItemDto(item1FromDB, new HashSet<>(), item1FromDB.getOwner().getId(), new ArrayList<>()));

        Assertions.assertEquals(item1FromGet, item1fromDBList);
    }
}
