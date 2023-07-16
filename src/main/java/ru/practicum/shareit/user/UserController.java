package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.exception.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Запрос на вывод спика пользователей");
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addNewUser(@Validated(Create.class) @RequestBody UserDto userDto) throws UserWithEmailAlreadyExists {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userService.addNewUser(userDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@Validated(Update.class) @RequestBody UserDto userDto, @PathVariable long id) throws UserWithEmailAlreadyExists, UserWithIdNotFound {
        log.info("Запрос на обновление пользователя с ID {}", id);
        return userService.updateUser(userDto, id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable long id) throws UserWithIdNotFound {
        log.info("Запрос на вывод пользователя с ID {}", id);
        return userService.getUserDto(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) throws UserWithIdNotFound {
        log.info("Запрос на удаление пользователя с ID {}", id);
        userService.deleteUser(id);
    }
}