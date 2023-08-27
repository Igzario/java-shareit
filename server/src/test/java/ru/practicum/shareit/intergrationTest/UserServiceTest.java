package ru.practicum.shareit.intergrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExists;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;
    private UserDto userDto = new UserDto();
    private UserDto user;
    UserDto userDto1;

    @BeforeEach
    public void init() throws EmailAlreadyExists {
        userDto.setName("Max");
        userDto.setEmail("qwe@qwe.com");
        user = userService.addNewUser(userDto);
    }

    @Test
    public void createUserTest() {
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void updateUserTest() throws EmailAlreadyExists, EntityNotFoundException {
        UserDto userDto2 = new UserDto();
        userDto2.setName("Pavel");
        UserDto userDto1 = userService.updateUser(userDto2, user.getId());

        assertThat(userDto1.getId(), notNullValue());
        assertThat(userDto1.getName(), equalTo("Pavel"));
        assertThat(userDto1.getEmail(), equalTo(userDto.getEmail()));

        try {
            userService.updateUser(userDto2, 99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }

        try {
            userDto2.setEmail("qwe@qwe.com");
            userService.updateUser(userDto2, user.getId());
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EmailAlreadyExists.class);
        }
    }

    @Test
    public void findUserTest() throws EntityNotFoundException {
        userDto1 = userService.findUserDtoById(user.getId());

        assertThat(userDto1.getId(), notNullValue());
        assertThat(userDto1.getName(), equalTo(userDto.getName()));
        assertThat(userDto1.getEmail(), equalTo(userDto.getEmail()));

        try {
            userDto1 = userService.findUserDtoById(99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }

    @Test
    public void getUsersTest() throws EmailAlreadyExists {
        UserDto userDto1 = new UserDto();
        userDto1.setName("Alex");
        userDto1.setEmail("f@q.com");

        userService.addNewUser(userDto1);
        List<UserDto> users = userService.getAllUsers();
        assertThat(users.size(), equalTo(2));

        try {
            userDto1.setEmail("qwe@qwe.com");
            userService.addNewUser(userDto1);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EmailAlreadyExists.class);
        }
    }

    @Test
    void deleteUserTest() throws EntityNotFoundException {
        userService.deleteUser(user.getId());
        try {
            userService.deleteUser(99L);
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass(), EntityNotFoundException.class);
        }
    }
}