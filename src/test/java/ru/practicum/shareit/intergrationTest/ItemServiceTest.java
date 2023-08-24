package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExists;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.UserNotHaveThisItemException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForOwner;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
public class ItemServiceTest {
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private UserServiceImpl userService;
    private UserDto userDto = new UserDto();
    private ItemDto itemDto;
    private UserDto userDto1;
    private ItemDto itemDto1;

    @BeforeEach
    public void init() throws EmailAlreadyExists, EntityNotFoundException {
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");
        itemDto = new ItemDto(1L, "ItemName", "Description", true, 2L);
        userDto1 = userService.addNewUser(userDto);
        itemDto1 = itemService.addNewItem(itemDto, userDto1.getId());
    }

    @Test
    public void createItemTest() {
        test(itemDto);
        assertThat(itemDto.getName(), equalTo(itemDto.getName()));
    }

    @Test
    public void patchItemTest() throws EntityNotFoundException, UserNotHaveThisItemException {
        ItemDto itemDtoCheck = new ItemDto(null, "newName", null, null, null);
        ItemDto itemDto1 = itemService.updateItem(itemDtoCheck, itemDto.getId(), userDto1.getId());
        test(itemDto1);
        assertThat(itemDto1.getName(), equalTo(itemDtoCheck.getName()));
    }

    @Test
    public void findByUserTest() throws EntityNotFoundException {
        ItemDto itemDtoCheck = new ItemDto(null, "ItemName2", "Description2", true, null);
        itemService.addNewItem(itemDtoCheck, userDto1.getId());
        List<ItemDtoForOwner> items = itemService.getItemsByUser(userDto1.getId());
        assertThat(items.size(), equalTo(2));
    }

    @Test
    public void searchItem() throws EntityNotFoundException {
        ItemDto itemDtoCheck = new ItemDto(null, "Desc", "Text", true, null);
        itemService.addNewItem(itemDtoCheck, userDto1.getId());
        List<ItemDto> items = itemService.searchItem(0L, "es");
        assertThat(items.size(), equalTo(1));
    }

    private void test(ItemDto itemDto) {
        assertThat(itemDto.getId(), notNullValue());
        assertThat(itemDto.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(itemDto.getAvailable()));
    }
}