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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.BusinessExceptionHandler;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    @Mock
    BookingService bookingService;

    @InjectMocks
    BookingController bookingController;

    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    UserDto userDto;
    UserDto userDto2;

    private String user1Name = "user1";
    private String user1Email = "user1@email";
    private Long user1Id = 1L;
    private String user1EmailUpdated = "user1Update@mail";

    private String user2Name = "user2";
    private String user2Email = "user2@email";
    private Long user2Id = 2L;

    ItemDto itemDto;

    private final Long itemId = 1L;
    private final String itemName = "item1";
    private final String itemDescription = "item1 description";
    private final Boolean itemAvailable = true;

    private BookingDtoInput bookingDtoInput;

    private BookingDto bookingDto;

    private final Long bookingId = 5L;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final LocalDateTime bookingStart = LocalDateTime.of(2023, 2, 6, 12, 21, 0, 0);
    private final LocalDateTime bookingEnd = LocalDateTime.of(2023, 2, 7, 12, 21, 0, 0);
    private BookingStatus bookingStatus = BookingStatus.WAITING;

    private static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @BeforeEach
    void init() {
        mapper.registerModule(new JavaTimeModule());

        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new
                MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(getMapper());

        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
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

        itemDto = new ItemDto();
        itemDto.setId(itemId);
        itemDto.setName(itemName);
        itemDto.setDescription(itemDescription);
        itemDto.setOwner(UserMapper.toUser(userDto));
        itemDto.setAvailable(itemAvailable);

        bookingDtoInput = new BookingDtoInput();
        bookingDtoInput.setItemId(itemId);
        bookingDtoInput.setStart(bookingStart);
        bookingDtoInput.setEnd(bookingEnd);

        bookingDto = new BookingDto();
        bookingDto.setId(bookingId);
        bookingDto.setStart(bookingStart);
        bookingDto.setEnd(bookingEnd);
        bookingDto.setItem(ItemMapper.toItem(itemDto));
        bookingDto.setBooker(UserMapper.toUser(userDto));
        bookingDto.setStatus(bookingStatus);
        bookingDto.setBookerId(user1Id);
    }

    @Test
    @DisplayName("Test add method")
    public void add() throws Exception {
        Mockito.when(bookingController.add(bookingDtoInput, user2Id)).thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoInput))
                        .header("X-Sharer-User-Id", user2Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.start").value(bookingStart.format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingEnd.format(formatter)))
                .andExpect(jsonPath("$.bookerId", is(user1Id), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.toString()), BookingStatus.class));
    }

    @Test
    @DisplayName("Test setApproveById method")
    public void setApproveById() throws Exception {
        bookingDto.setStatus(BookingStatus.APPROVED);

        Mockito.when(bookingController.setApproveById(bookingId, true, user1Id)).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/".concat(bookingId.toString()).concat("?approved=true"))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.start").value(bookingStart.format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingEnd.format(formatter)))
                .andExpect(jsonPath("$.bookerId", is(user1Id), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.toString()), BookingStatus.class));
    }

    @Test
    @DisplayName("Test getById method")
    public void getById() throws Exception {
        Mockito.when(bookingController.getById(bookingId, user1Id)).thenReturn(bookingDto);

        mvc.perform(get("/bookings/".concat(bookingId.toString()))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.start").value(bookingStart.format(formatter)))
                .andExpect(jsonPath("$.end").value(bookingEnd.format(formatter)))
                .andExpect(jsonPath("$.bookerId", is(user1Id), Long.class))
                .andExpect(jsonPath("$.status", is(BookingStatus.WAITING.toString()), BookingStatus.class));
    }

    @Test
    @DisplayName("Test getByUserId method")
    public void getByUserId() throws Exception {
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(bookingDto);

        Mockito.when(bookingController.getByUserId(user1Id, Optional.of(BookingState.ALL.toString()), 0, 10))
                .thenReturn(bookings);

        mvc.perform(get("/bookings".concat("?state=ALL"))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.[0].start").value(bookingStart.format(formatter)))
                .andExpect(jsonPath("$.[0].end").value(bookingEnd.format(formatter)))
                .andExpect(jsonPath("$.[0].bookerId", is(user1Id), Long.class))
                .andExpect(jsonPath("$.[0].status", is(BookingStatus.WAITING.toString()), BookingStatus.class));
    }

    @Test
    @DisplayName("Test getByItemsByUserId method")
    public void getByItemsByUserId() throws Exception {
        List<BookingDto> bookings = new ArrayList<>();
        bookings.add(bookingDto);

        Mockito.when(bookingController.getByItemsByUserId(user1Id, Optional.of(BookingState.ALL.toString()), 0, 10))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner".concat("?state=ALL"))
                        .header("X-Sharer-User-Id", user1Id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingId), Long.class))
                .andExpect(jsonPath("$.[0].start").value(bookingStart.format(formatter)))
                .andExpect(jsonPath("$.[0].end").value(bookingEnd.format(formatter)))
                .andExpect(jsonPath("$.[0].bookerId", is(user1Id), Long.class))
                .andExpect(jsonPath("$.[0].status", is(BookingStatus.WAITING.toString()), BookingStatus.class));
    }
}
