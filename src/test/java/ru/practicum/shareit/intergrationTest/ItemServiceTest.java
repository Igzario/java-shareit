package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
    private ItemDto itemDtoCheck;

    @BeforeEach
    public void init() throws EmailAlreadyExists, EntityNotFoundException {
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("ItemName");
        itemDto.setDescription("Description");
        itemDto.setAvailable(true);
        itemDto.setRequestId(2L);

        itemDtoCheck = new ItemDto();
        itemDtoCheck.setId(null);
        itemDtoCheck.setName("newName");
        itemDtoCheck.setDescription("Desc");
        itemDtoCheck.setRequestId(null);
        itemDtoCheck.setAvailable(true);

        userDto1 = userService.addNewUser(userDto);
        itemDto1 = itemService.addNewItem(itemDto, userDto1.getId());
    }

    @Test
    public void createItemTest() {
        assertThat(itemDto1, equalTo(itemDto));
        try {
            itemService.addNewItem(itemDto, 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void patchItemTest() throws EntityNotFoundException, UserNotHaveThisItemException {
        ItemDto itemDto1 = itemService.updateItem(itemDtoCheck, itemDto.getId(), userDto1.getId());
        assertThat(itemDtoCheck, equalTo(itemDto1));
        try {
            itemService.updateItem(itemDtoCheck, 99L, userDto1.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
        try {
            itemService.updateItem(itemDtoCheck, itemDto.getId(), 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), UserNotHaveThisItemException.class);
        }
    }

    @Test
    public void findByUserTest() throws EntityNotFoundException {
        itemService.addNewItem(itemDtoCheck, userDto1.getId());
        List<ItemDtoForOwner> items = itemService.getItemsByUser(userDto1.getId());
        assertThat(items.size(), equalTo(2));
        try {
            items = itemService.getItemsByUser(99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void searchItem() throws EntityNotFoundException {
        itemService.addNewItem(itemDtoCheck, userDto1.getId());
        List<ItemDto> items = itemService.searchItem(0L, "es");
        assertThat(items.size(), equalTo(2));
        try {
            items = itemService.searchItem(0L, "s");
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }
}