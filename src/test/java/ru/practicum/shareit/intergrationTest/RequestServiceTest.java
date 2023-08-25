package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.itemRequest.ItemRequestService;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;
    @Autowired
    private ItemRequestService requestService;

    private UserDto userDto;
    private UserDto userDto1;
    private ItemRequestDto itemRequestDto;
    private List<ItemDto> items = new ArrayList<>();
    List<ItemRequestDto> requests;

    @BeforeEach
    public void init() throws EmailAlreadyExists, EntityNotFoundException {
        userDto = new UserDto();
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        userDto1 = userService.addNewUser(userDto);
        ItemDto itemDtoCreate = new ItemDto();
        itemDtoCreate.setId(null);
        itemDtoCreate.setName("Mr. Booker");
        itemDtoCreate.setDescription("Description");
        itemDtoCreate.setAvailable(true);
        itemDtoCreate.setRequestId(null);

        UserDto userDtoOwner = new UserDto();
        userDtoOwner.setName("Owner");
        userDtoOwner.setEmail("qw11e@qwe.com");
        userDtoOwner = userService.addNewUser(userDtoOwner);
        items.add(itemService.addNewItem(itemDtoCreate, userDtoOwner.getId()));

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        itemRequestDto.setItems(items);
    }

    @Test
    public void createRequestTest() throws EntityNotFoundException {
        ItemRequestDto itemRequestDto1 = requestService.addItemRequest(itemRequestDto, userDto1.getId());
        test(itemRequestDto1);
    }

    @Test
    public void createRequestTestException() {
        try {
            itemRequestDto.setId(99L);
            requestService.addItemRequest(itemRequestDto, userDto1.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
        try {
            requestService.addItemRequest(itemRequestDto, 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void getUserRequestsTest() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());

        requests = requestService.getRequests(userDto1.getId());

        assertThat(requests.size(), equalTo(1));
    }

    @Test
    public void getUserRequestsTestException() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());
        try {
            requestService.getRequests(99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void getOtherUsersRequestsTest() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());

        requests = requestService.getItemRequests(userDto1.getId(), 0, 10);

        assertThat(requests.size(), equalTo(0));
    }

    @Test
    public void getOtherUsersRequestsTestException() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());
        try {
            requestService.getItemRequests(99L, 0, 10);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void getOneRequestTest() throws EntityNotFoundException {
        ItemRequestDto itemRequestDtoWithId = requestService.addItemRequest(itemRequestDto, userDto1.getId());

        ItemRequestDto itemRequestDto1 = requestService.getItemRequest(itemRequestDtoWithId.getId(), userDto1.getId());

        test(itemRequestDto1);
    }

    @Test
    public void getOneRequestTestException() throws EntityNotFoundException {
        ItemRequestDto itemRequestDtoWithId = requestService.addItemRequest(itemRequestDto, userDto1.getId());

        try {
            requestService.getItemRequest(99L, userDto1.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
        try {
            requestService.getItemRequest(itemRequestDtoWithId.getId(), 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    private void test(ItemRequestDto itemRequestDtoTest) {
        assertThat(itemRequestDtoTest.getId(), notNullValue());
        assertThat(itemRequestDtoTest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}