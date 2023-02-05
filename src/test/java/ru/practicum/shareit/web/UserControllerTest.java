package ru.practicum.shareit.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.BusinessExceptionHandler;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private String user1Name = "user1";
    private String user1Email = "user1@email";
    private Long user1Id = 1L;
    private String user1EmailUpdated = "user1Update@mail";

    private String user2Name = "user2";
    private String user2Email = "user2@email";
    private Long user2Id = 2L;

    @BeforeEach
    void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(BusinessExceptionHandler.class)
                .build();
    }

    @Test
    public void save() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        Mockito.when(userService.save(Mockito.any(UserDto.class))).thenReturn(userDto);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1Id), Long.class))
                .andExpect(jsonPath("$.name", is(user1Name), String.class))
                .andExpect(jsonPath("$.email", is(user1Email), String.class));
    }

    @Test
    public void getById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        Mockito.when(userService.getById(user1Id)).thenReturn(userDto);

        mvc.perform(get("/users/".concat(user1Id.toString()))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1Id), Long.class))
                .andExpect(jsonPath("$.name", is(user1Name), String.class))
                .andExpect(jsonPath("$.email", is(user1Email), String.class));
    }

    @Test
    public void update() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        UserDto userUpdatedDto = new UserDto();
        userUpdatedDto.setId(user1Id);
        userUpdatedDto.setName(user1Name);
        userUpdatedDto.setEmail(user1EmailUpdated);

        Mockito.when(userService.update(userUpdatedDto, user1Id)).thenReturn(userUpdatedDto);

        mvc.perform(patch("/users/".concat(user1Id.toString()))
                .content(mapper.writeValueAsString(userUpdatedDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user1Id), Long.class))
                .andExpect(jsonPath("$.name", is(user1Name), String.class))
                .andExpect(jsonPath("$.email", is(user1EmailUpdated), String.class));
    }

    @Test
    public void deleteById() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        Mockito.when(userService.getById(user1Id)).thenThrow(UserNotFoundException.class);
//        Mockito.doNothing().when(userService).delete(user1Id);

        mvc.perform(delete("/users/".concat(user1Id.toString()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/users/".concat(user1Id.toString()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(user1Id);
        userDto.setName(user1Name);
        userDto.setEmail(user1Email);

        UserDto userDto2 = new UserDto();
        userDto2.setEmail(user2Email);
        userDto2.setName(user2Name);
        userDto2.setId(user2Id);

        List<UserDto> users = new ArrayList<>();
        users.add(userDto);
        users.add(userDto2);

        Mockito.when(userService.getAll()).thenReturn(users);

        mvc.perform(get("/users")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].id", is(user1Id), Long.class))
                .andExpect(jsonPath("$.[0].name", is(user1Name), String.class))
                .andExpect(jsonPath("$.[0].email", is(user1Email), String.class))
                .andExpect(jsonPath("$.[1].id", is(user2Id), Long.class))
                .andExpect(jsonPath("$.[1].name", is(user2Name), String.class))
                .andExpect(jsonPath("$.[1].email", is(user2Email), String.class));
    }
}
