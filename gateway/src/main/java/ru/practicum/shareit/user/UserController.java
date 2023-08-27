package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validated.*;
import ru.practicum.shareit.user.dto.UserDto;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Запрос на вывод спика пользователей");
        return userClient.getAll();

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addNewUser(@Validated @RequestBody UserDto userDto) {
        log.info("Запрос на добавление пользователя {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateUser(
            @Validated(Update.class) @RequestBody UserDto userDto,
            @PathVariable long id) {
        log.info("Запрос на обновление пользователя с ID {}", id);
        return userClient.update(id, userDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        log.info("Запрос на вывод пользователя с ID {}", id);
        return userClient.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable long id) {
        log.info("Запрос на удаление пользователя с ID {}", id);
        userClient.delete(id);
    }
}