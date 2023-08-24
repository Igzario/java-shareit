package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
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

    @BeforeEach
    public void init() throws EmailAlreadyExists, EntityNotFoundException {
        userDto = new UserDto();
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        userDto1 = userService.addNewUser(userDto);
        ItemDto itemDtoCreate = new ItemDto(null, "Mr. Booker", "Description", true, null);

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
    public void getUserRequestsTest() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());

        List<ItemRequestDto> requests = requestService.getRequests(userDto1.getId());

        assertThat(requests.size(), equalTo(1));
    }

    @Test
    public void getOtherUsersRequestsTest() throws EntityNotFoundException {
        requestService.addItemRequest(itemRequestDto, userDto1.getId());

        List<ItemRequestDto> requests = requestService.getItemRequests(userDto1.getId(), 0, 10);

        assertThat(requests.size(), equalTo(0));
    }

    @Test
    public void getOneRequestTest() throws EntityNotFoundException {
        ItemRequestDto itemRequestDtoWithId = requestService.addItemRequest(itemRequestDto, userDto1.getId());

        ItemRequestDto itemRequestDto1 = requestService.getItemRequest(itemRequestDtoWithId.getId(), userDto1.getId());

        test(itemRequestDto1);
    }

    private void test(ItemRequestDto itemRequestDtoTest) {
        assertThat(itemRequestDtoTest.getId(), notNullValue());
        assertThat(itemRequestDtoTest.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}