package ru.practicum.shareit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.BusinessExceptionHandler;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestsControllerText {
    @Mock
    ItemRequestService itemRequestService;

    @InjectMocks
    ItemRequestController itemRequestController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    UserDto userDto;
    UserDto userDto2;

    private final String user1Name = "user1";
    private final String user1Email = "user1@email";
    private final Long user1Id = 1L;

    private final String user2Name = "user2";
    private final String user2Email = "user2@email";
    private final Long user2Id = 2L;

    ItemRequestDtoInput itemRequestDtoInput;

    private final String requestDescription = "request for item1 description";

    ItemRequestDto itemRequestDto;

    private final Long requestId = 5L;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final LocalDateTime requestCreated = LocalDateTime.of(2023, 2, 5, 12, 21, 0, 0);

    ItemDto itemDto;

    private final Long itemId = 1L;
    private final String itemName = "item1";
    private final String itemDescription = "item1 description";
    private final Boolean itemAvailable = true;

    List<ItemDto> items = new ArrayList<>();

    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @BeforeEach
    public void init() {
        mapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(getMapper());

        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .setControllerAdvice(BusinessExceptionHandler.class)
                .setMessageConverters(mappingJackson2HttpMessageConverter)
                .build();

        userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        userDto2 = new UserDto();
        userDto2.setEmail(user2Email);
        userDto2.setName(user2Name);
        userDto2.setId(user2Id);

        itemRequestDtoInput = new ItemRequestDtoInput();
        itemRequestDtoInput.setDescription(requestDescription);

        items.add(itemDto);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(requestId);
        itemRequestDto.setCreated(requestCreated);
        itemRequestDto.setDescription(requestDescription);
        itemRequestDto.setItems(items);

        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName(itemName);
        itemDto.setDescription(itemDescription);
        itemDto.setOwner(UserMapper.toUser(userDto));
        itemDto.setAvailable(itemAvailable);
        itemDto.setRequestId(requestId);
    }

    @Test
    @DisplayName("Test add method")
    public void add() throws Exception {
        Mockito.when(itemRequestController.add(user2Id, itemRequestDtoInput)).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoInput))
                        .header("X-Sharer-User-Id", user2Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.created").value(requestCreated.format(formatter)))
                .andExpect(jsonPath("$.description").value(requestDescription))
                .andExpect(jsonPath("$.items").value(items));
    }

    @Test
    @DisplayName("Test getByRequestId method")
    public void getByRequestId() throws Exception {
        Mockito.when(itemRequestController.getByRequestId(user2Id, requestId)).thenReturn(itemRequestDto);

        mvc.perform(get("/requests".concat("/" + requestId))
                        .header("X-Sharer-User-Id", user2Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId))
                .andExpect(jsonPath("$.created").value(requestCreated.format(formatter)))
                .andExpect(jsonPath("$.description").value(requestDescription))
                .andExpect(jsonPath("$.items").value(items));
    }

    @Test
    @DisplayName("Test getByRequestorId method")
    public void getByRequestorId() throws Exception {
        List<ItemRequestDto> requests = new ArrayList<>();
        requests.add(itemRequestDto);

        Mockito.when(itemRequestController.getByRequestorId(user2Id)).thenReturn(requests);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", user2Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(requestId))
                .andExpect(jsonPath("$.[0].created").value(requestCreated.format(formatter)))
                .andExpect(jsonPath("$.[0].description").value(requestDescription))
                .andExpect(jsonPath("$.[0].items").value(items));
    }

    @Test
    @DisplayName("Test getAllByPages method")
    public void getAllByPages() throws Exception {
        List<ItemRequestDto> requests = new ArrayList<>();
        requests.add(itemRequestDto);

        Mockito.when(itemRequestController.getAllByPages(user2Id, 0, 10)).thenReturn(requests);

        mvc.perform(get("/requests".concat("/all"))
                        .header("X-Sharer-User-Id", user2Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(requestId))
                .andExpect(jsonPath("$.[0].created").value(requestCreated.format(formatter)))
                .andExpect(jsonPath("$.[0].description").value(requestDescription))
                .andExpect(jsonPath("$.[0].items").value(items));
    }
}
