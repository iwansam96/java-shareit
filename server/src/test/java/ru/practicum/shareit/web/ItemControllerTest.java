package ru.practicum.shareit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoInput;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exception.BusinessExceptionHandler;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    ItemService itemService;

    @Mock
    CommentService commentService;

    @InjectMocks
    ItemController itemController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    UserDto userDto;
    UserDto userDto2;

    private final String user1Name = "user1";
    private final String user1Email = "user1@email";
    private final Long user1Id = 1L;
    private final String user1EmailUpdated = "user1Update@mail";

    private final String user2Name = "user2";
    private final String user2Email = "user2@email";
    private final Long user2Id = 2L;

    private ItemDto itemDto;
    private ItemDto itemDto2;

    private final Long itemId = 1L;
    private final String itemName = "item1";
    private final String itemDescription = "item1 description";
    private final Boolean itemAvailable = true;

    private final Long item2Id = 2L;
    private final String item2Name = "item2";
    private final String item2Description = "item2 description";
    private final Boolean item2Available = true;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .setControllerAdvice(BusinessExceptionHandler.class)
                .build();

        userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        userDto2 = new UserDto();
        userDto2.setEmail(user2Email);
        userDto2.setName(user2Name);
        userDto2.setId(user2Id);

        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName(itemName);
        itemDto.setDescription(itemDescription);
        itemDto.setOwner(UserMapper.toUser(userDto));
        itemDto.setAvailable(itemAvailable);

        itemDto2 = new ItemDto();
        itemDto2.setId(item2Id);
        itemDto2.setName(item2Name);
        itemDto2.setDescription(item2Description);
        itemDto2.setOwner(UserMapper.toUser(userDto2));
        itemDto2.setAvailable(item2Available);
    }

    @Test
    @DisplayName("Test add method")
    public void add() throws Exception {
        Mockito.when(itemService.add(user1Id, itemDto)).thenReturn(itemDto);

        mvc.perform(post("/items")
                    .header("X-Sharer-User-Id", user1Id.toString())
                    .content(mapper.writeValueAsString(itemDto))
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemName), String.class))
                .andExpect(jsonPath("$.description", is(itemDescription), String.class));
    }

    @Test
    @DisplayName("Test edit method")
    public void edit() throws Exception {
        ItemDto itemDtoUpdated = new ItemDto();
        itemDtoUpdated.setId(itemId);
        itemDtoUpdated.setName(itemName);
        itemDtoUpdated.setDescription(itemDescription.concat(" updated"));
        itemDtoUpdated.setOwner(UserMapper.toUser(userDto));
        itemDtoUpdated.setAvailable(itemAvailable);

        Mockito.when(itemService.edit(user1Id, itemId, itemDtoUpdated)).thenReturn(itemDtoUpdated);

        mvc.perform(patch("/items/".concat(itemId.toString()))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .content(mapper.writeValueAsString(itemDtoUpdated))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemName), String.class))
                .andExpect(jsonPath("$.description", is(itemDescription.concat(" updated")), String.class));
    }

    @Test
    @DisplayName("Test getById method")
    public void getById() throws Exception {
        Mockito.when(itemService.getById(itemId, user1Id)).thenReturn(itemDto);

        mvc.perform(get("/items/".concat(itemId.toString()))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemId), Long.class))
                .andExpect(jsonPath("$.name", is(itemName), String.class))
                .andExpect(jsonPath("$.description", is(itemDescription), String.class));
    }

    @Test
    @DisplayName("Test getByOwnerId method")
    public void getByOwnerId() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto);
        items.add(itemDto2);

        Mockito.when(itemService.getByOwnerId(user1Id, 0, 10)).thenReturn(items);

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemId), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemName), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDescription), String.class))
                .andExpect(jsonPath("$.[1].id", is(item2Id), Long.class))
                .andExpect(jsonPath("$.[1].name", is(item2Name), String.class))
                .andExpect(jsonPath("$.[1].description", is(item2Description), String.class));
    }

    @Test
    @DisplayName("Test search method")
    public void search() throws Exception {
        List<ItemDto> items = new ArrayList<>();
        items.add(itemDto2);

        Mockito.when(itemService.search("item2", 0, 10)).thenReturn(items);

        mvc.perform(get("/items/search".concat("?text=item2"))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(item2Id), Long.class))
                .andExpect(jsonPath("$.[0].name", is(item2Name), String.class))
                .andExpect(jsonPath("$.[0].description", is(item2Description), String.class));
    }

    @Test
    @DisplayName("Test addComment method")
    public void addComment() throws Exception {
        String commentText = "comment1 for item1";

        CommentDtoInput commentDtoInput = new CommentDtoInput();
        commentDtoInput.setText(commentText);

        Long commentId = 5L;

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentId);
        commentDto.setText(commentText);
        commentDto.setCreated(LocalDateTime.now().minusDays(1));
        commentDto.setAuthorName(user2Name);

        Mockito.when(commentService.addComment(user1Id, itemId, commentDtoInput)).thenReturn(commentDto);

        mvc.perform(post("/items/".concat(itemId.toString()).concat("/comment"))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .content(mapper.writeValueAsString(commentDtoInput))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentId), Long.class))
                .andExpect(jsonPath("$.text", is(commentText), String.class))
                .andExpect(jsonPath("$.authorName", is(user2Name), String.class));
    }
}
