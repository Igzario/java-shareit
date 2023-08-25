package ru.practicum.shareit.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoFromRequest;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Autowired
    protected MockMvc mvcItem;
    @Autowired
    protected MockMvc mvcUser;
    @Autowired
    protected MockMvc mvcBooking;
    @Autowired
    protected ObjectMapper mapper = new ObjectMapper();
    @Mock
    ItemService itemService;
    @Mock
    UserService userService;
    @Mock
    BookingService bookingService;
    @InjectMocks
    private ItemController itemController;
    @InjectMocks
    private UserController userController;
    @InjectMocks
    private BookingController bookingController;
    private ItemDto itemDto;
    private UserDto userDto;
    private ItemDto itemDto2;

    @BeforeEach
    void setUp() throws Exception {
        mapper.registerModule(new JSR310Module());
        mvcBooking = MockMvcBuilders
                .standaloneSetup(bookingController)
                .build();
        mvcItem = MockMvcBuilders
                .standaloneSetup(itemController)
                .build();

        mvcUser = MockMvcBuilders
                .standaloneSetup(userController)
                .build();

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("ItemName");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);


        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        when(userService.addNewUser(any())).thenReturn(userDto);
        mvcUser.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("Max")));
    }

    @Test
    void addItem() throws Exception {
        when(itemService.addNewItem(any(), any())).thenReturn(itemDto);
        mvcItem.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("Description")));
    }

    @Test
    void updateItem() throws Exception {
        addItem();
        itemDto2 = new ItemDto();
        itemDto2.setId(1L);
        itemDto2.setName("UpdateItemName");
        itemDto2.setDescription("UpdateDescription");
        itemDto2.setAvailable(true);

        when(itemService.updateItem(any(), anyLong(), anyLong())).thenReturn(itemDto2);
        mvcItem.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto2))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("UpdateItemName")))
                .andExpect(jsonPath("$.description", is("UpdateDescription")));
    }

    @Test
    void getItem() throws Exception {
        addItem();
        when(itemService.getItemDto(anyLong(), anyLong())).thenReturn(itemDto);
        mvcItem.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.name", is("ItemName")))
                .andExpect(jsonPath("$.description", is("Description")));
    }

    @Test
    void addComment() throws Exception {
        addItem();
        addBooking();
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setAuthorName("name");
        commentDto.setCreated(LocalDateTime.now());

        when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(commentDto);

        mvcItem.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1L), Long.class))
                .andExpect(jsonPath("$.text", is("text")))
                .andExpect(jsonPath("$.authorName", is("name")));
    }

    @Test
    void addBooking() throws Exception {
        addItem();
        BookingDtoFromRequest bookingDtoFromRequest = new BookingDtoFromRequest(1L, LocalDateTime.now().plusSeconds(5), LocalDateTime.now().plusSeconds(7));
        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusSeconds(10));
        bookingDto.setId(1L);
        bookingService.addBooking(bookingDtoFromRequest, 1L);
    }
}